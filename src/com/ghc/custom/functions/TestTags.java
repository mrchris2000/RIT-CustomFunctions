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
 * "/project/@name" (update the value of the "name" 
 * attribute in the "project" element.)
 * 
 * "/project/test[last()]/@dude" (update the value of the 
 * "dude" attribute in the last element called "test")
 * 
 * "/project/test[@price>50.00]" (update the value of the test 
 * content for all test elements that have an attribute called "price" 
 * whose value is greater than 50.00 )
 * 
 * "/project/test[@price>50.00]/@dude" (update the value of the 
 * "dude" attribute for all test elements that have an attribute 
 * called "price" whose value is greater than 50.00 )
 * 
 * "/project/test[@dude='old']" (update the value of the 
 * "test" element for all test elements that have an attribute 
 * called "dude" whose value is equal to the string 'old')
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
public class TestTags extends Function
{

   Function m_fXmlInput;

   Function m_fXPathInput;

   Function m_fValueInput;

   public TestTags()
   {

   }

   public TestTags( Function f1, Function f2, Function f3 )
   {
      this.m_fXmlInput = f1;
      this.m_fXPathInput = f2;
      this.m_fValueInput = f3;
   }

   @SuppressWarnings("rawtypes")
   @Override
   public Function create( int size, Vector params )
   {
      return new TestTags( (Function)params.get( 0 ),
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
      TestTags xpu = new TestTags();
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
