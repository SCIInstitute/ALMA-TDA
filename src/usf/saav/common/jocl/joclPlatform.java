package usf.saav.common.jocl;

import static org.jocl.CL.CL_CONTEXT_PLATFORM;
import static org.jocl.CL.CL_DEVICE_TYPE_ALL;
import static org.jocl.CL.CL_PLATFORM_NAME;
import static org.jocl.CL.clGetDeviceIDs;

import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_platform_id;

public class joclPlatform {

	joclDevice [] devices;
    final long deviceType = CL_DEVICE_TYPE_ALL;
	private cl_platform_id platform_id;

	public joclPlatform(cl_platform_id platform_id) {
        
        this.platform_id = platform_id;

        // Initialize the context properties
        cl_context_properties contextProperties = new cl_context_properties();
        contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform_id);
        
        // Obtain the number of devices for the platform
        int numDevicesArray[] = new int[1];
        clGetDeviceIDs(platform_id, deviceType, 0, null, numDevicesArray);
        int numDevices = numDevicesArray[0];
        
        this.devices = new joclDevice[numDevices];
        
        // Obtain a device ID 
        cl_device_id _devices[] = new cl_device_id[numDevices];
        clGetDeviceIDs(platform_id, deviceType, numDevices, _devices, null);
        for(int i = 0; i < devices.length; i++){
        	devices[i] = new joclDevice( contextProperties, _devices[i] );
        }

	}

	@Override
	public String toString(){
		StringBuilder ret = new StringBuilder( );
		
        String platformName = joclController.getString(platform_id, CL_PLATFORM_NAME);

        ret.append("Number of devices in platform "+platformName+": "+devices.length +"\n\n");

        for( joclDevice d : devices ){
        	ret.append( d.toString() );
        }
        return ret.toString();
        
	}

	public joclDevice getDevice(int i) {
		return devices[i];
	}
	
	public int getDeviceCount(){
		return devices.length;
	}
}
