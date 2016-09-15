package usf.saav.common.jocl;

import static org.jocl.CL.clEnqueueReadBuffer;

import org.jocl.CL;
import org.jocl.Pointer;
import org.jocl.cl_event;
import org.jocl.cl_mem;

public class joclMemory {

	private joclDevice device;
	cl_mem     memory;

	
	joclMemory(joclDevice device, long flags, long size, Pointer host_data, int[] err_codes){
		this.device = device;
		this.memory = CL.clCreateBuffer( device.context, flags, size, host_data, err_codes );
	}
	
	public void release( ){
		CL.clReleaseMemObject(memory);
		memory = null;
	}
	

	public void EnqueueWriteBuffer( boolean blocking_write, long offset, long cb, Pointer ptr, int num_events_in_wait_list, cl_event[] event_wait_list, cl_event event ) throws joclException {
		if( memory == null ) return;
		if( CL.clEnqueueWriteBuffer( device.commandQueue, memory, blocking_write, offset, cb, ptr, num_events_in_wait_list, event_wait_list, event) != CL.CL_SUCCESS ){
			throw new joclException();
		}
	}

	public void EnqueueWriteBuffer( boolean blocking_write, float [] data, int num_events_in_wait_list, cl_event[] event_wait_list, cl_event event ) throws joclException {
		if( memory == null ) return;
		if( CL.clEnqueueWriteBuffer( device.commandQueue, memory, blocking_write, 0, data.length*4, Pointer.to(data), num_events_in_wait_list, event_wait_list, event) != CL.CL_SUCCESS ){
			throw new joclException();
		}
	}
	
	public void EnqueueWriteBuffer( boolean blocking_write, float [] data ) throws joclException {
		if( memory == null ) return;
		if( CL.clEnqueueWriteBuffer( device.commandQueue, memory, blocking_write, 0, data.length*4, Pointer.to(data), 0, null, null ) != CL.CL_SUCCESS ){
			throw new joclException();
		}
	}
	

	public void EnqueueReadBuffer( boolean blocking, float [] dst ) throws joclException {
		if( memory == null ) return;
		if( clEnqueueReadBuffer( device.commandQueue, memory, blocking, 0, 4 * dst.length, Pointer.to(dst), 0, null, null) != CL.CL_SUCCESS ){
			throw new joclException();
		}
	}
	
	public void EnqueueReadBuffer( boolean blocking, int [] dst ) throws joclException {
		if( memory == null ) return;
		if( clEnqueueReadBuffer( device.commandQueue, memory, blocking, 0, 4 * dst.length, Pointer.to(dst), 0, null, null) != CL.CL_SUCCESS ){
			throw new joclException();
		}
	}

	public void EnqueueReadBuffer( boolean blocking, byte [] dst ) throws joclException {
		if( memory == null ) return;
		if( clEnqueueReadBuffer( device.commandQueue, memory, blocking, 0, dst.length, Pointer.to(dst), 0, null, null) != CL.CL_SUCCESS ){
			throw new joclException();
		}
	}
	

	
}
