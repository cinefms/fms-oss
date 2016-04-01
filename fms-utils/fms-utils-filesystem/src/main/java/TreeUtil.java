import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.openfms.utils.filesystem.entities.Directory;

public class TreeUtil {

	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
		ObjectMapper om = new ObjectMapper();
		om.enable(SerializationFeature.INDENT_OUTPUT);
		if(args.length==0) {
			System.err.println("usage: TreeUtil path [outfile]");
			System.exit(-1);
		} 

		long start = System.currentTimeMillis();
		Directory d = Directory.create(args[0]);
		if(args.length==1) {
			om.writeValue(System.out, d);
		} else {
			System.err.println("writing output to: "+new File(args[1]).getAbsolutePath());
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			om.writeValue(baos, d);
			FileUtils.writeByteArrayToFile(new File(args[1]), baos.toByteArray());
			System.err.println("checked "+d.getFiles().size()+" files in "+(System.currentTimeMillis()-start)+"ms");
		}
		
		
	}

}
