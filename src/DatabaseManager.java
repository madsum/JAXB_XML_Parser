import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

	private String url = "jdbc:oracle:thin:@localhost:1521:XE";
	private String user = "system";
	private String pass = "dev";
	private static Connection conn;
	long uniqeIndexErrorCode = 23000l; 

	public DatabaseManager() {
		try {
			conn = DriverManager.getConnection(url, user, pass);
			if (conn != null) {
				System.out.println("Connected to the database!");
			} else {
				System.out.println("Failed to make connection!");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public long insertData(InteriorResponse interiorResponse) {
		long masterRetVal = -1l;
		try {			
			for(ColorUpholstery colUph : interiorResponse.getColorUpholsteryList()) {
				String sqlMasterQuery = makeSqlMasterInsert(interiorResponse.getStartWeek(), 
		        											interiorResponse.getEndWeek(), 
		        											interiorResponse.getPno12(), 
		        											colUph.getColor(), 
		        											colUph.getUpholstery());
				masterRetVal = insertIntoInteriorMaster(conn, sqlMasterQuery);
				if(masterRetVal == uniqeIndexErrorCode) {
					System.out.println("This row already exit in the table. Handle error");
				}
			}
		}catch (Exception e) {
			System.out.println("Error when doing  insert . Handle error "+e.getMessage());
        }
		long retVal = insertCommonFeaturData(interiorResponse, masterRetVal);
		if(retVal == -1){
			System.out.println("Error to insert feature data");
		}
		retVal = insertIndividualFeature(interiorResponse, masterRetVal);
		if(retVal == -1){
			System.out.println("Error to insert individual data");
		}
		return masterRetVal;
	}
	
	public long insertCommonFeaturData(InteriorResponse interiorResponse, long masterId) {
		long retVal = -1l;
		for(Feature feature : interiorResponse.getFeatureList()) {
			String sqlFeatureQuery = makeSqlFeatureInsert(masterId, feature.getCode(), null, "code unknown", "1");
            //System.out.println(sqlFeatureQuery);
            retVal =  insertIntoInteriorFeature(conn, sqlFeatureQuery);
            System.out.println("Common feature insert: "+retVal);
            if(retVal == -1l) {
               	System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
            }
		}
		for(Integer option : interiorResponse.getOptionList()) {
			String sqlFeatureQuery = makeSqlFeatureInsert(masterId, option, null, "code unknown", "1");
            //System.out.println(sqlFeatureQuery);
            retVal =  insertIntoInteriorFeature(conn, sqlFeatureQuery);
            System.out.println("Common option insert: "+retVal);
            if(retVal == -1l) {
               	System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
            }
		}
		if(retVal == -1){
			System.out.println("Error to insert common data");
		}
		return retVal;		
	}
	
	public long insertIndividualFeature(InteriorResponse interiorResponse, long masterId){
		long retVal = -1;
		for(InteriorRoom interiorRoom : interiorResponse.getCuList()) {
			for(Feature feature : interiorRoom.getFeatureList()) {
				String sqlFeatureQuery = makeSqlFeatureInsert(masterId, feature.getCode(), null, "code unknown", "0");
	            //System.out.println(sqlFeatureQuery);
	            retVal =  insertIntoInteriorFeature(conn, sqlFeatureQuery);
	            System.out.println("Individual feature insert : "+retVal);
	            if(retVal == -1l) {
	               	System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
	            }
			}
			for(Option option : interiorRoom.getOptionList()) {
				String sqlFeatureQuery = makeSqlFeatureInsert(masterId, option.getCode(), option.getState(), "code unknown", "0");
	            //System.out.println(sqlFeatureQuery);
	            retVal =  insertIntoInteriorFeature(conn, sqlFeatureQuery);
	            System.out.println("individual option insert: "+retVal);
	            if(retVal == -1l) {
	               	System.out.println("Error when insert into INTERIOR_ROOMS_FEATURES check it. Handle error");
	            }
			}	
		}
		return retVal;
	}
	public static String makeSqlFeatureInsert(long masterId, long dataElement, String state, String code, String common)
	{
		String sqlInsertPart = "INSERT INTO INTERIOR_ROOMS_FEATURES (MASTER_ROOM_ID, DATA_ELEMENT,STATE,CODE, COMMON) VALUES(";
		String sqlQeuery;
		sqlQeuery = String.format(sqlInsertPart + "%d, %d, '%s', '%s', '%s' )", masterId, dataElement, state, code, common);
		//System.out.println(sqlQeuery);
		return sqlQeuery;
	}

	
	public static String makeSqlMasterInsert(int startWeek, int endWeek, String pno12, String color, String upholstery)
	{
		String sqlInsertPart = "INSERT INTO INTERIOR_ROOMS_MASTER(STR_WEEK_FROM, STR_WEEK_TO, PNO12, COLOR, UPHOLSTERY) VALUES(";
		String sqlQeuery;
		sqlQeuery = String.format(sqlInsertPart + "%d, %d, '%s', '%s', '%s' )", startWeek, endWeek, pno12, color, upholstery);
		//System.out.println(sqlQeuery);
		return sqlQeuery;
	}
	
	public static Long insertIntoInteriorMaster(Connection conn, String sqlQuery) {
		String key[] = {"ROOM_ID"}; 
		Long retValue = -1l;
		try {
			PreparedStatement ps = conn.prepareStatement(sqlQuery, key);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				retValue = rs.getLong(1);
			   System.out.println("Master Primary key: "+retValue);
			}
		}catch (SQLException e) {
			// if e.getSQLState() returns 23000. It means integrity constraint violation of the unique index
			retValue = convertErroCode(e.getSQLState());
        } catch (Exception e) {
            System.out.println("Exception when insert into INTERIOR_ROOMS_MASTER");
        }
		return retValue;
	}
	
	public static Long convertErroCode(String error) {
		long errorCode = -1;
		try {
			errorCode = (long) Long.parseLong(error);
		}catch(Exception ex){
			System.out.println("Excepiton to convert sql error code string to long");
		}
		return errorCode;
	}
	
	public static Long insertIntoInteriorFeature(Connection conn, String sqlQuery) { 
		Long retValue = -1l;
		try {
			PreparedStatement ps = conn.prepareStatement(sqlQuery);
			retValue = (long) ps.executeUpdate();
		}catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }catch (Exception e) {
            System.out.println("Exception when insert into INTERIOR_ROOMS_FEATURES");
        }
		return retValue;
	}
}
