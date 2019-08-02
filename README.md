# JAXB_XML_Parser
JAXB example


I don't need any extra wrapper class for FeatuerList and OptionList. It can be done by the JAXB annotation @XmlElementWrapper(name = "FeatureList"). Also, a very important lesson learned. We have to mark all the property's getter method as @XmlTransient. Otherwise, JAXB throws an exception 2 properties found with the same name. Because our class all properties is visible to the JAXB. So we have to mark one as @XmlTransient.
