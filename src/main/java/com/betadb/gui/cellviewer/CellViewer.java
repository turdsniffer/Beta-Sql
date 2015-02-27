/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * CellViewer.java
 *
 * Created on Nov 7, 2011, 10:05:55 AM
 */
package com.betadb.gui.cellviewer;


import com.google.inject.Singleton;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import static java.util.logging.Logger.getLogger;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

/**
 *
 * @author parmstrong
 */
@Singleton
public class CellViewer extends javax.swing.JFrame
{
	private final RSyntaxTextArea textViewer;
	private final JScrollPane scrPane;

	/**
	 * Creates new form CellViewer
	 */
	public CellViewer()
	{
		this.setTitle("Cell Details");
		this.setPreferredSize(new Dimension(600, 450));
		textViewer =  new RSyntaxTextArea();
		JPopupMenu popupMenu = textViewer.getPopupMenu();
		popupMenu.addSeparator();
		JMenuItem btnFormatXML = new JMenuItem("Format XML");
		btnFormatXML.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent ae)
			{
				CellViewer.this.formatTextAsXml();
			}
		});
		
		popupMenu.add(btnFormatXML);

		scrPane = new JScrollPane(textViewer);
		this.add(scrPane);
		initComponents();
	}

	public void setValue(Class cellClass, Object value)
	{
		textViewer.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		textViewer.setText(value.toString());
		this.setVisible(true);
		this.setState(JFrame.NORMAL);

	}

	void formatTextAsXml()
	{
		String text = textViewer.getText();
		String displayString = getPrettyPrintXml(text);
		textViewer.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
		textViewer.setText(displayString);
	}

	private String getPrettyPrintXml(String xml)
	{
		String xmlString = "";
		try
		{
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
			StreamResult result = new StreamResult(new StringWriter());
			InputSource is = new InputSource(new StringReader(xml));
			Document document = builder.parse(is);
			Source source = new DOMSource(document);
			transformer.transform(source, result);
			xmlString = result.getWriter().toString();
		}
		catch (ParserConfigurationException | IllegalArgumentException | SAXException | IOException | TransformerException ex)
		{
			getLogger(CellViewer.class.getName()).log(Level.SEVERE, null, ex);
		}
		return xmlString;
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents
	/**
	 * @param args the command line arguments
	 */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
