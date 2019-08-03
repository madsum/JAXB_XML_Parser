import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Test {
	
	private static InteriorResponse interiorResponse;
	static long uniqeIndexErrorCode = 23000l; 

    public static void main(String[] args) {
        UnmarshalXml();
        insertIntoDB();       
    }
    
    static void UnmarshalXml() {
        File file = new File("minxml2.xml");
        JAXBContext jaxbContext;

        try {
            jaxbContext = JAXBContext.newInstance(InteriorResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            interiorResponse = (InteriorResponse) unmarshaller.unmarshal(file);
            List<InteriorRoom> cuList = interiorResponse.getCuList();
            if(!cuList.isEmpty()) {
            	for (InteriorRoom colUph : cuList) {
            		interiorResponse.addColorUpholstery(colUph.getColor(), colUph.getUpholstery());
            	}
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
    
    static void insertIntoDB() {
    	try {
            DatabaseManager dbMgr = new DatabaseManager();
            long retVal = dbMgr.insertData(interiorResponse);
            if(retVal == uniqeIndexErrorCode || retVal == -1 ) {
            	System.out.println("This row already exit in the table. Handle error");
            	return;
            }
    	}catch (Exception e) {
    		System.out.println("Error when insert data check it. Handle error");
        }
    }
    
    static void printData() {
        System.out.println("StartWeek: " + interiorResponse.getStartWeek());
        System.out.println("EndWeek: " + interiorResponse.getEndWeek());
        System.out.println("Pno12: " + interiorResponse.getPno12());

        List<Feature> featureList = interiorResponse.getFeatureList();
        for (Feature feature : featureList) {
            System.out.println("Common feature code: " + feature.getCode());
        }

        List<Integer> optionList = interiorResponse.getOptionList();
        for (int option : optionList) {
            System.out.println("Common Option: " + option);
        }

        List<InteriorRoom> cuList = interiorResponse.getCuList();
        if(!cuList.isEmpty()) {
        	for (InteriorRoom colUph : cuList) {
        		interiorResponse.addColorUpholstery(colUph.getColor(), colUph.getUpholstery());
        	}
        }
        for (InteriorRoom colUph : cuList) {
            System.out.println("Color: " + colUph.getColor());
            System.out.println("Upholstery: " + colUph.getUpholstery());
            System.out.println(colUph.getColor() + " features:- ");
            for (Feature feature : colUph.getFeatureList()) {
                System.out.println("ColUph's feature code: " + feature.getCode());
            }
            System.out.println(colUph.getColor() + " ColUph's Optoine:- ");
            for (Option option : colUph.getOptionList()) {
                System.out.println("Optoine code: " + option.getCode());
                System.out.println("Optoine state: " + option.getState());
            }
        }    	
    }
 }
