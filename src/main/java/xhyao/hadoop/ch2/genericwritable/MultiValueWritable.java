package xhyao.hadoop.ch2.genericwritable;

import org.apache.hadoop.io.GenericWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * HTTP server log processing sample for the Chapter 4 of Hadoop MapReduce
 * Cookbook. 
 * 
 * @author Thilina Gunarathne
 */
public class MultiValueWritable extends GenericWritable {

	@SuppressWarnings("rawtypes")
	private static Class[] CLASSES =  new Class[]{
		IntWritable.class,
		Text.class
	};
	
	public MultiValueWritable(){		
	}
	
	public MultiValueWritable(Writable value){
		set(value);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Class[] getTypes() {
		return CLASSES;
	}
}
