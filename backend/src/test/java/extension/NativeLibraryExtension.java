package extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.opencv.core.Core;

public class NativeLibraryExtension implements TestInstancePostProcessor {
     @Override
     public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
          System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
     }
}
