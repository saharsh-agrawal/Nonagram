import java.io.File;
import java.io.IOException;
import java.io.InputStream;

final public class ResourceLoader {
	
	public static InputStream read(String path) throws IOException
	{
		return ResourceLoader.class.getResourceAsStream(path);
	}
}