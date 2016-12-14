/**
 * 
 */
package usf.saav.alma.data.fits;

import java.io.IOException;

/**
 * @author dwhite
 *
 */
public interface CacheInterface {

	boolean isSliceLoaded(int z) throws IOException;

	void setSliceLoaded(int z) throws IOException;

	void writeBackAll() throws IOException;

	void setValue(long index, float value) throws IOException;

	float getValue(long index) throws IOException;

}
