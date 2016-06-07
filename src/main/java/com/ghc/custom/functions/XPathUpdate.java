package com.ghc.custom.functions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ghc.ghTester.expressions.EvalUtils;
import com.ghc.ghTester.expressions.Function;

/**
 * Changes a value in an XML file on the path to the value passed in. The node
 * to update can be either an element or an attribute. If you want to update an
 * attribute, just pass <code>null</code> for it and the parser will look just
 * for the matching element name. Not intended to solve every XML update
 * problem, but just handle some of the boilerplate in certain cases.
 * <p>
 * <b>Examples:</b><br/>
 * 
 * <pre>
 * 
 * &quot;/project/@name&quot; (update the value of the &quot;name&quot; 
 * attribute in the &quot;project&quot; element.)
 * 
 * &quot;/project/test[last()]/@dude&quot; (update the value of the 
 * &quot;dude&quot; attribute in the last element called &quot;test&quot;)
 * 
 * &quot;/project/test[@price&gt;50.00]&quot; (update the value of the test 
 * content for all test elements that have an attribute called &quot;price&quot; 
 * whose value is greater than 50.00 )
 * 
 * &quot;/project/test[@price&gt;50.00]/@dude&quot; (update the value of the 
 * &quot;dude&quot; attribute for all test elements that have an attribute 
 * called &quot;price&quot; whose value is greater than 50.00 )
 * 
 * &quot;/project/test[@dude='old']&quot; (update the value of the 
 * &quot;test&quot; element for all test elements that have an attribute 
 * called &quot;dude&quot; whose value is equal to the string 'old')
 * </pre>
 * 
 * </p>
 * <p>
 * This implementation uses DOM, so it may not be suitable for very large (many
 * megabyte) files.
 * 
 * @param fileIn
 *            the available XML file you want to update.
 *            
 * @param xpathExpression
 *            the valid XPath expression to determine which nodes will get
 *            updated.
 * 
 *            See <a
 *            href="link http://www.w3schools.com/xpath/xpath_syntax.asp">W3
 *            Schools XML Tutorial</a> for details on XPath syntax.
 *            
 * @param newValue
 *            the string you want to set this tag or attribute to.
 *            
 * @throws IOException
 *             if anything goes wrong, this wraps all of the other exception
 *             types possible from setting up the factories and transformers.
 */
public class XPathUpdate extends Function
{

   Function m_fXmlInput;

   Function m_fXPathInput;

   Function m_fValueInput;

   public XPathUpdate()
   {

   }

   public XPathUpdate( Function f1, Function f2, Function f3 )
   {
      this.m_fXmlInput = f1;
      this.m_fXPathInput = f2;
      this.m_fValueInput = f3;
   }

   @Override
   public Function create( int size, Vector params )
   {
      return new XPathUpdate( (Function)params.get( 0 ),
            (Function)params.get( 1 ), (Function)params.get( 2 ) );
   }

   @Override
   public synchronized Object evaluate( Object data )
   {
      String inputXML = m_fXmlInput.evaluateAsString( data );
      String xpath = EvalUtils
            .getString( m_fXPathInput.evaluateAsString( data ) );
      String value = m_fValueInput.evaluateAsString( data );
      try
      {
         return updateValueInXml( inputXML, xpath, value );
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
      return null;
   }

   public String updateValueInXml( String xmlIn, String xpathExpression,
                                   String newValue ) throws IOException
   {

      // Set up the DOM evaluator
      final DocumentBuilderFactory docFactory = DocumentBuilderFactory
            .newInstance();

      try
      {
         final DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
         byte[] byteArray = xmlIn.getBytes();
         ByteArrayInputStream baos = new ByteArrayInputStream( byteArray );

         final Document doc = docBuilder.parse( baos );

         final XPath xpath = XPathFactory.newInstance().newXPath();
         NodeList nodes = (NodeList)xpath.evaluate( xpathExpression, doc,
               XPathConstants.NODESET );

         // Update the nodes we found
         for ( int i = 0, len = nodes.getLength(); i < len; i++ )
         {
            Node node = nodes.item( i );
            node.setTextContent( newValue );
         }

         // Get file ready to write
         final Transformer transformer = TransformerFactory.newInstance()
               .newTransformer();
         transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
         transformer.setOutputProperty( OutputKeys.ENCODING, "UTF-8" );

         StringWriter writer = new StringWriter();
         StreamResult result = new StreamResult( writer );
         transformer.transform( new DOMSource( doc ), result );

         // Write file out
         //result.getWriter().flush();

         return ( writer.toString() );

      }
      catch ( XPathExpressionException xpee )
      {
         throw new IOException( "Cannot parse XPath.", xpee );
      }
      catch ( DOMException dome )
      {
         throw new IOException( "Cannot create DOM tree", dome );
      }
      catch ( TransformerConfigurationException tce )
      {
         throw new IOException( "Cannot create transformer.", tce );
      }
      catch ( IllegalArgumentException iae )
      {
         throw new IOException( "Illegal Argument.", iae );
      }
      catch ( ParserConfigurationException pce )
      {
         throw new IOException( "Cannot create parser.", pce );
      }
      catch ( SAXException saxe )
      {
         throw new IOException( "Error reading XML document.", saxe );
      }
      catch ( TransformerFactoryConfigurationError tfce )
      {
         throw new IOException( "Cannot create transformer factory.", tfce );
      }
      catch ( TransformerException te )
      {
         throw new IOException( "Cannot write values.", te );
      }
   }

   public static void main( String[] args )
   {
      XPathUpdate xpu = new XPathUpdate();
      try
      {
         System.out
               .println( xpu
                     .updateValueInXml(
                           "<?xml version=\"1.0\" ?> <earth>     <country>us</country> </earth>",
                           "/earth/country", "Azerbaijan" ) );
      }
      catch ( Exception e )
      {
         e.printStackTrace();
      }
   }
}
