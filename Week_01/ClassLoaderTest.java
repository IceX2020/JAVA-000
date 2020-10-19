import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassLoaderTest extends ClassLoader {
    public static void main(String[] args){
        try {
            Class clazz = new ClassLoaderTest().findClass("Hello");
            Method hello = clazz.getMethod("hello");
            hello.invoke(clazz.newInstance());    
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File file = new File("D://Hello.xlass");
        InputStream in = null;
        byte[] bytes = new byte[1024];
        int bytelength = -1;

        try {
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                if((bytelength++) >= bytes.length){
                    bytes = addLengthArray(bytes);
                }
                bytes[bytelength] = (byte)(255-tempbyte);
            }
            System.out.println(bytelength);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return defineClass(name,bytes,0,bytelength+1);
    }
    public static byte[] addLengthArray(byte[] array) {
        byte[] newArray = new byte[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }
}
