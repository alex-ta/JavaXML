import org.w3c.dom.Element;


public class MAIN {
	
	public static void main(String[]args){
		XML xmlobjekt = new XML();
		
		XML.sysout(xmlobjekt.read("./NewFile.xml"));
		
		
		Element e = xmlobjekt.addRoot("root");
		e = xmlobjekt.addElement(e, "bahn");
		xmlobjekt.addAttribute(e, "name", "Railbahn");
		xmlobjekt.write("./file.xml");
	}
}
