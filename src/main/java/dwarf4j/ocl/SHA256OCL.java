package dwarf4j.ocl;

import static org.jocl.CL.*;
import org.jocl.*;

public class SHA256OCL {

  public SHA256OCL() { }

  @SuppressWarnings({ "resource", "deprecation" })
  public void teste() {

    int[] prefixoHeader = new int[] { 2, 0, 0, 0, 23, 151, 91, 151, 193, 142, 209, 247, 226, 85, 173, 242, 151, 89, 155,
        85, 51, 14, 218, 184, 120, 3, 200, 23, 1, 0, 0, 0, 0, 0, 0, 0, 138, 151, 41, 90, 39, 71, 180, 241, 160, 179,
        148, 141, 243, 153, 3, 68, 192, 225, 159, 166, 178, 185, 43, 58, 25, 200, 230, 186, 220, 20, 23, 135, 53, 139,
        5, 83, 83, 95, 1, 25, };
    int[] tamanhoHeaderEalvo = new int[] { prefixoHeader.length, 695 };
    int[] noncePremiada = new int[1];

    Pointer ponteiroPrefixoHeader = Pointer.to(prefixoHeader);
    Pointer ponteiroHeaderEalvo = Pointer.to(tamanhoHeaderEalvo);
    Pointer ponteiroNoncePremiada = Pointer.to(noncePremiada);

    final int platformIndex = 0;
    final long deviceType = CL_DEVICE_TYPE_ALL;
    final int deviceIndex = 0;
    CL.setExceptionsEnabled(true);
    int numPlatformsArray[] = new int[1];
    clGetPlatformIDs(0, null, numPlatformsArray);
    int numPlatforms = numPlatformsArray[0];
    cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
    clGetPlatformIDs(platforms.length, platforms, null);
    cl_platform_id platform = platforms[platformIndex];
    cl_context_properties contextProperties = new cl_context_properties();
    contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
    int numDevicesArray[] = new int[1];
    clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
    int numDevices = numDevicesArray[0];
    cl_device_id devices[] = new cl_device_id[numDevices];
    clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
    cl_device_id device = devices[deviceIndex];
    cl_context context = clCreateContext(contextProperties, 1, new cl_device_id[] { device }, null, null, null);
    cl_command_queue commandQueue = clCreateCommandQueue(context, device, 0, null);

    cl_mem memObjects[] = new cl_mem[3];
    memObjects[0] = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * prefixoHeader.length, ponteiroPrefixoHeader, null);
    memObjects[1] = clCreateBuffer(context, CL_MEM_READ_ONLY | CL_MEM_COPY_HOST_PTR, Sizeof.cl_int * 2, ponteiroHeaderEalvo, null);
    memObjects[2] = clCreateBuffer(context, CL_MEM_READ_WRITE, Sizeof.cl_int, null, null);

    String codigoFonte = null;
    try {
      codigoFonte = new java.util.Scanner(ClassLoader.getSystemResource("dwarf4j.cl").openStream()).useDelimiter("\\A").next();
    }
    catch (java.io.IOException e) {
      System.err.println(e.getMessage());
      System.exit(-1);
    }

    // Create the program from the source code
    cl_program program = clCreateProgramWithSource(context, 1, new String[] { codigoFonte }, null, null);

    // Build the program
    clBuildProgram(program, 0, null, null, null, null);

    // Create the kernel
    cl_kernel kernel = clCreateKernel(program, "dwarf4j", null);

    // Set the arguments for the kernel
    final int qtdArgumentos = 3;
    for(int i = 0; i < qtdArgumentos; i++) clSetKernelArg(kernel, i, Sizeof.cl_mem, Pointer.to(memObjects[i]));

    clEnqueueNDRangeKernel(commandQueue, kernel, 1, null, new long[] { 1 }, new long[] { 1 }, 0, null, null);

    clEnqueueReadBuffer(commandQueue, memObjects[2], CL_TRUE, 0, Sizeof.cl_int, ponteiroNoncePremiada, 0, null, null);

    for(int i = 0; i < qtdArgumentos; i++) clReleaseMemObject(memObjects[i]);

    clReleaseKernel(kernel);
    clReleaseProgram(program);
    clReleaseCommandQueue(commandQueue);
    clReleaseContext(context);

    System.out.println(noncePremiada[0]);

  }

  public static String inverterString(String entrada) {
    StringBuilder saida = new StringBuilder();
    for(int i = entrada.length() - 1; i >= 0; i--)
      saida.append(entrada.charAt(i));
    return saida.toString();
  }

}
