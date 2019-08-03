import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class Option {

    @XmlElement(name = "Code")
    private int code;
    @XmlElement(name = "state")
    private String state;

    @XmlTransient
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @XmlTransient
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Option [code=" + code + ", state=" + state + "]";
    }

}
