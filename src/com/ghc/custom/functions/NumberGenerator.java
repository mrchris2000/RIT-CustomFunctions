package com.ghc.custom.functions;

import com.ghc.ghTester.expressions.*;

import java.util.Vector;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.Charset;

/**
 * The function takes two parameters. Param1: Result length (ie 6 would result
 * in 000001, 3 would result in 001).
 * <p/>
 * NumberGenerator( length )
 * <p/>
 * Example:
 * <p/>
 * NumberGenerator( 6 ) returns: 000001
 * <p/>
 * Param2: Optional path to a file to read and store the last used number.
 * <p/>
 * NumberGenerator( length[, path])
 * <p/>
 * Example:
 * <p/>
 * NumberGenerator( 6, c:\\temp\\myFile.txt ) returns: 000001 then: 000002 and
 * increments on subsequent executions.
 * <p/>
 */
public class NumberGenerator extends Function {
	/**
	 * This function will return the number that we wish to output.
	 */
	private Function m_fNumberGeneratorNumber = null;
	/**
	 * This function will return the length that we wish to output.
	 */
	private Function m_fNumberGeneratorLength = null;

	private Function m_fFileName;

	private File file;

	/**
	 * Constructor used to register this function.
	 */
	public NumberGenerator(Function f1, Function f2) {
		m_fFileName = null;
		m_fNumberGeneratorLength = null;
		m_fNumberGeneratorLength = f1;
		m_fFileName = f2;
	}

	public NumberGenerator(Function f1) {
		m_fFileName = null;
		m_fNumberGeneratorLength = null;
		m_fNumberGeneratorLength = f1;
	}

	public NumberGenerator() {
		m_fFileName = null;
		m_fNumberGeneratorLength = null;
		try {
			// Default if no path specified
			file = new File("c:\\\\numberGenerator.tmp");
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write("0");
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Evaluate method is used to do the meat of the work. This will always
	 * return 'done' to the caller regardless of success or failure
	 *
	 * @param data
	 *            - this should be passed to all evaluate calls
	 */
	public synchronized Object evaluate(Object data) {
		// StringWriter responseString = new StringWriter();
		String newNumber = "";
		String filename;
		FileLock lock;
		newNumber = "";
		filename = "";
		lock = null;

		String len = m_fNumberGeneratorLength.evaluateAsString(data);
		try {
			filename = m_fFileName.evaluateAsString(data);
		} catch(Exception noFilename){
			filename = "";
		}
		try {
			if (filename.equals(""))
				filename = "c:\\\\numberGenerator.tmp";
			System.out.println((new StringBuilder("Output to file: ")).append(
					filename).toString());
			int length = (new Integer(len)).intValue();
			file = new File(filename);
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter out = new BufferedWriter(new FileWriter(file));
				out.write("0");
				out.flush();
				out.close();
			}

			FileChannel channel = (new RandomAccessFile(file, "rw"))
					.getChannel();
			while (lock == null)
				try {
					lock = channel.tryLock();
				} catch (Exception exception1) {
				}
			ByteBuffer buff = ByteBuffer.allocate(0x80000);
			buff.position(0);
			Charset cs = Charset.forName("ASCII");
			channel.read(buff, 0L);
			buff.flip();
			CharBuffer charbuff = cs.decode(buff);
			String previousNumber = charbuff.toString();
			if (previousNumber.equals(""))
				previousNumber = "0";
			int prev = (new Integer(previousNumber)).intValue();
			prev++;
			for (newNumber = new String((new StringBuilder()).append(prev)
					.toString()); newNumber.length() < length; newNumber = "0"
					.concat(newNumber))
				;
			buff.clear();
			buff.put(newNumber.getBytes());
			buff.flip();
			channel.write(buff, 0L);
			channel.close();
			channel = null;
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return newNumber;
	}

	/**
	 * This function is called when an expression is being evaluated and a new
	 * instance of this function needs to be created.
	 *
	 * @param size
	 *            number of params
	 * @param params
	 *            a vector of Function objects, which are the parameters
	 */
	public Function create(int size, Vector params) {
		if (params.size() > 1)
			return new NumberGenerator((Function) params.get(0),
					(Function) params.get(1));
		else
			return new NumberGenerator((Function) params.get(0));
	}
}