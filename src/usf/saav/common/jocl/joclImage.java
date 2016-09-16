package usf.saav.common.jocl;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.cl_image_desc;
import org.jocl.cl_image_format;
import org.jocl.cl_mem;

public class joclImage {

	private joclDevice device;
	cl_mem     memory;
	cl_image_format img_format;
	cl_image_desc img_desc;

	
	public joclImage(joclDevice device, long flags, cl_image_format img_format, cl_image_desc img_desc, Pointer data, int[] ciErrNum) {
		this.device = device;
		this.img_format = img_format;
		this.img_desc = img_desc;
		this.memory = CL.clCreateImage( device.context, flags, img_format, img_desc, data, ciErrNum );
	}

	public void release( ){
		CL.clReleaseMemObject(memory);
		memory = null;
	}

	public void EnqueueWriteImage(boolean blocking, float[] img) throws joclException {
		if( CL.clEnqueueWriteImage( device.commandQueue, 
								memory,
				                blocking,
				                new long[]{0,0,0},
				                new long[]{img_desc.image_width,img_desc.image_height,img_desc.image_depth},
				                img_desc.image_row_pitch,
				                img_desc.image_slice_pitch,
				                Pointer.to(img),
				                0, null, null ) != CL.CL_SUCCESS ){
			throw new joclException();
		}
		
	}
	


}
