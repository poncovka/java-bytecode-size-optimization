package jbyco.optimization;

import java.io.IOException;

/**
 * Created by vendy on 3.5.16.
 */
public interface Optimizer {

    public byte[] optimizeClassFile(byte[] bytes) throws IOException;

}
