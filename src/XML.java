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
	private Document in,out;
	private DocumentBuilder built = null;
    
	
	public XML(){
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
		try{
			in = built.parse(new File(path));
			} catch (Exception e){
			e.printStackTrace();	
			}
		return in;
	}
	
	public Element addRoot(String tagName){
		Element e =out.createElement(tagName);
		out.appendChild(e);
		return e;
	}
	public Element addElement(Element parentname,String tagName){
		Element e =out.createElement(tagName);
		parentname.appendChild(e);
		return e;
	}
	public void addAttribute(Element tagName,String Attname,String value){
		tagName.setAttribute(Attname, value);
	}
	
	public boolean write(String path){
		DOMImplementation imp = out.getImplementation();
		DOMImplementationLS add= (DOMImplementationLS) imp.getFeature("LS", "3.0");
		LSSerializer writer = add.createLSSerializer();
		writer.writeToURI(out, "file:src/"+path);
		return true;
	}
	
	public static void sysout (Document doc){
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
		public Node node;
		public int level;
		public MappingObj(Node item, int level) {
			this.node = item;
			this.level = level;
		}
	}
	
	class IntegerObj{
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

