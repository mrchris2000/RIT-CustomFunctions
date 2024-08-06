package com.ghc.custom.functions;

import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

//import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;

public class TestTagStore extends Function{

	private Function m_fNum1;
	public TestTagStore(){
				
	}
	
	public TestTagStore(Function f1){
		
		m_fNum1 = f1;		

	}
	
	//Simple String concatenation
	public Object evaluate(Object data) {
		
		String test = m_fNum1.evaluateAsString(data);
		
		//verify the path exists before going any further
		
		String rtn = "";

		try {

		    File fXmlFile = new File(test);
		    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    Document doc = dBuilder.parse(fXmlFile);
		            
		    //optional, but recommended
		    //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
		    doc.getDocumentElement().normalize();

		    System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		            
		    NodeList nList = doc.getElementsByTagName("staff");
		            
		    System.out.println("----------------------------");

		    for (int temp = 0; temp < nList.getLength(); temp++) {

		        Node nNode = nList.item(temp);
		                
		        System.out.println("\nCurrent Element :" + nNode.getNodeName());
		                
		        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

		            Element eElement = (Element) nNode;

		            System.out.println("Tag Data Store : " + eElement.getAttribute("tagDataStore"));
		            
		            rtn=eElement.getAttribute("tagDataStore");
		            
		        }
		    }
		    } catch (Exception e) {
		    e.printStackTrace();
		    }		
				
		return rtn;
	}
	
	@SuppressWarnings("rawtypes")
	public Function create(int size, Vector params) {
        return new TestTagStore((Function) params.get(0));
    }	

}
