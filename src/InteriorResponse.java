import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name = "Features_res")
public class InteriorResponse {

    @XmlElement(name = "StartWeek")
    private int startWeek;

    @XmlElement(name = "EndWeek")
    private int endWeek;

    @XmlElement(name = "Pno12")
    private String pno12;

    @XmlElementWrapper(name = "FeatureList")
    @XmlElement(name = "Feature")
    private List<Feature> featureList;

    @XmlElementWrapper(name = "OptionList")
    @XmlElement(name = "Option")
    private List<String> optionList;

    @XmlElementWrapper(name = "CUList")
    @XmlElement(name = "CU")
    private List<InteriorRoom> cuList;

    public InteriorResponse() {
    }

    public InteriorResponse(int startWeek, int endWeek, String pno12) {
        super();
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.pno12 = pno12;
    }

    @XmlTransient
    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    @XmlTransient
    public int getEndWeek() {
        return endWeek;
    }

    public void setEndWeek(int endWeek) {
        this.endWeek = endWeek;
    }

    @XmlTransient
    public String getPno12() {
        return pno12;
    }

    public void setPno12(String pno12) {
        this.pno12 = pno12;
    }

    @XmlTransient
    public List<Feature> getFeatureList() {
        return featureList;
    }

    public void setFeatureList(List<Feature> featureList) {
        this.featureList = featureList;
    }

    @XmlTransient
    public List<String> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<String> optionList) {
        this.optionList = optionList;
    }

    @XmlTransient
    public List<InteriorRoom> getCuList() {
        return cuList;
    }

    public void setCuList(List<InteriorRoom> cuList) {
        this.cuList = cuList;
    }

    @Override
    public String toString() {
        return "InteriorResponse{" + "sWeek=" + startWeek + ", eWeek=" + endWeek + ", pno12='" + pno12 + '\'' + ", featureList=" + featureList + ", optionList="
            + optionList + '}';
    }
}