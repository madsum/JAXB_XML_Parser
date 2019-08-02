import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Test {

    public static void main(String[] args) {
        File file = new File("minxml.xml");
        JAXBContext jaxbContext;

        try {
            jaxbContext = JAXBContext.newInstance(InteriorResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            InteriorResponse interiorResponse = (InteriorResponse) unmarshaller.unmarshal(file);
            System.out.println("StartWeek: " + interiorResponse.getStartWeek());
            System.out.println("EndWeek: " + interiorResponse.getEndWeek());
            System.out.println("Pno12: " + interiorResponse.getPno12());

            List<Feature> featureList = interiorResponse.getFeatureList();
            for (Feature feature : featureList) {
                System.out.println("Common feature code: " + feature.getCode());
            }

            List<String> optionList = interiorResponse.getOptionList();
            for (String option : optionList) {
                System.out.println("Common Option: " + option);
            }

            List<InteriorRoom> cuList = interiorResponse.getCuList();
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
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}