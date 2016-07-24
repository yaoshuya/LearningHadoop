/**
 * Following sample is adopted from original wordcount sample from 
 * http://wiki.apache.org/hadoop/WordCount. 
 */
package xhyao.hadoop.ch1;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;


/**
 * <p>The word count sample counts the number of word occurrences within a set of input documents 
 * using MapReduce. The code has three parts: mapper, reducer, and the main program.</p>
 * @author Srinath Perera (srinath@wso2.com)
 */
public class WordCount {

  /**
   * <p>
   * The mapper extends from the org.apache.hadoop.mapreduce.Mapper interface. When Hadoop runs, 
   * it receives each new line in the input files as an input to the mapper. The �map� function 
   * tokenize the line, and for each token (word) emits (word,1) as the output.  </p>
   */
  public static class TokenizerMapper 
       extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
      
    public void map(Object key, Text value, Context context
                    ) throws IOException, InterruptedException {
      StringTokenizer itr = new StringTokenizer(value.toString());
      while (itr.hasMoreTokens()) {
        word.set(itr.nextToken());
        context.write(word, one);
      }
    }
  }
  
  /**
   * <p>Reduce function receives all the values that has the same key as the input, and it output the key 
   * and the number of occurrences of the key as the output.</p>  
   */
  public static class IntSumReducer 
       extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  /**
   * <p> As input this program takes any text file. Create a folder called input in HDFS (or in local directory if you are running this locally)
   * <ol>
   * <li>Option1: You can compile the sample by ant from sample directory.  To do this, you need to have Apache Ant installed in your system. 
   * Otherwise, you can use the complied jar included with the source code. hange directory to HADOOP_HOME, and copy the hadoop-cookbook.jar to the HADOOP_HOME. 
   * Then run the command > bin/hadoop jar hadoop-cookbook.jar chapter1.WordCount input output.</li> 
   * <li>As an optional step, copy the �input� directory to the top level of the IDE based project (eclipse project) that you created for samples. Now you can run 
   * the WordCount class directly from your IDE passing �input output� as arguments. This will run the sample same as before. Running MapReduce Jobs from IDE in this manner is very useful 
   * for debugging your MapReduce Jobs. </li>
   * </ol> 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
//	conf.addResource("core-site.xml");
//	conf.addResource("hdfs-site.xml");
//	conf.addResource("yarn-site.xml");
//	conf.addResource("mapred-site.xml");
	
    conf.set("mapred.job.tracker", "local"); 
    
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: wordcount <in> <out>");
      System.exit(2);
    }
    Job job = Job.getInstance(conf, "word count");
    job.setJarByClass(WordCount.class);
    job.setMapperClass(TokenizerMapper.class);
    
    /**** Uncomment the following line to enable the Combiner ****/
    //job.setCombinerClass(IntSumReducer.class);
    
    /**** Delete the target path if exists ****/
    Path outPath = new Path(args[1]); 
    FileSystem dfs = FileSystem.get(outPath.toUri(), conf); 
    if (dfs.exists(outPath))  dfs.delete(outPath, true); 
   
    //String path2jar = "/home/student/workspace/learningHadoop/target/learningHadoop-0.0.1-SNAPSHOT.jar";
    //job.addFileToClassPath(new Path(path2jar)); 
    
    job.setReducerClass(IntSumReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}


