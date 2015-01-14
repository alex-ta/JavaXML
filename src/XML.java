import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;


public class XML {
	/**
	 * @author Alex
	 * XML Klasse die die XML Logik einigermaßen kapselt
	 * in ein XML Dokument das XML daten beinhalten kann 
	 * out ein XML Dokument das XML daten beinhalten kann 
	 * */
	
	private Document in,out;
	private DocumentBuilder built = null;
    
	
	public XML(){
		/**
		 * Erstellen einer Document Builder Instance, bei der die
		 * überprüfung angestellt wird. Zuletzt wird das out Dokument 
		 * erzeugt, das später beschrieben werden kann.
		 * */
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		fac.setValidating(true);
		try {
			built = fac.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		}
		try{
			out = built.newDocument();
			} catch (Exception e){
			e.printStackTrace();	
			}
	}
	
	public Document read(String path){
		/***
		 * Diese Methode liest eine XML Datei über
		 * die Pfadangabe ein und speichert die Werte im
		 * Dokument in.
		 * */
		try{
			in = built.parse(new File(path));
			} catch (Exception e){
			e.printStackTrace();	
			}
		return in;
	}
	
	public Element addRoot(String tagName){
		/**
		 * schreibt ein Wurzelelement auf das out Dokument
		 * */
		Element e =out.createElement(tagName);
		out.appendChild(e);
		return e;
	}
	public Element addElement(Element parentname,String tagName){
		/**
		 * schreibt ein Element auf das out Dokument
		 * */
		Element e =out.createElement(tagName);
		parentname.appendChild(e);
		return e;
	}
	public void addAttribute(Element tagName,String Attname,String value){
		/**
		 * schreibt Attribut auf das out Dokument
		 * */
		tagName.setAttribute(Attname, value);
	}
	
	public boolean write(String path){
		/**
		 * schreibt das out Dokument an die path Adresse
		 * */
		DOMImplementation imp = out.getImplementation();
		DOMImplementationLS add= (DOMImplementationLS) imp.getFeature("LS", "3.0");
		LSSerializer writer = add.createLSSerializer();
		writer.writeToURI(out, "file:src/"+path);
		return true;
	}
	
	public static void sysout (Document doc){
		/**
		 * gibt ein XML Document das eingelesen wurde auf der Console aus
		 * dazu wird eine Liste aller Kinder eines Elements erstellt.
		 * dazu wird die Ebene der Hirachie in der Variable level abgelegt.
		 * die Kindelemente der Wurzel werden in der Methode abgearbeitet,
		 * haben diese wieder Kind Elemente kommt Rekursion durch die Funktion
		 * rek aufgerufen.
		 * */
		LinkedList<MappingObj> childes = new LinkedList<MappingObj>();
		IntegerObj level = new IntegerObj(0);
		childes.add(new MappingObj(doc.getDocumentElement(),level.value));
		level.inc();
		NodeList ls = doc.getDocumentElement().getChildNodes();
		for(int i=0; i<ls.getLength();i++){
			Node n = ls.item(i);
			childes.add(new MappingObj(n,level.value));	
			rek(ls.item(i),childes,level);
		}
		
		for(MappingObj m : childes){
			if(m.node.getNodeName().equals("#text"))
				continue;
			System.out.println("Element----------");
			System.out.println("Level: "+m.level+" Nodename "+m.node.getNodeName());
			
			NamedNodeMap ns = m.node.getAttributes();
			System.out.println("---Attribute-----");
			for(int i =0; i<ns.getLength();i++){
				Node a = ns.item(i);
				System.out.println("    "+a.getNodeName()+":"+a.getNodeValue());
			}
			System.out.println("");
		}
	}
	
	private static void rek(Node n,List<MappingObj> list, IntegerObj level){	
		/**
		 * sammelt alle Kindelemente des Elements
		 * */
			NodeList ls = n.getChildNodes();
			level.inc();
			for(int i =0; i<ls.getLength();i++){
				list.add(new MappingObj(ls.item(i),level.value));	
				rek(ls.item(i),list,level);
			}
			level.dec();
	}
}

	class MappingObj{
		/**
		 * Objekt das ein Element und desen Hierachieebene speichert
		 * */
		public Node node;
		public int level;
		public MappingObj(Node item, int level) {
			this.node = item;
			this.level = level;
		}
	}
	
	class IntegerObj{
		/**Integer per Referenz*/
		int value;
		public IntegerObj(int value){
			this.value = value;
		}
		public void inc(){
			value++;
		}
		public void dec(){
			value--;
		}
	}

