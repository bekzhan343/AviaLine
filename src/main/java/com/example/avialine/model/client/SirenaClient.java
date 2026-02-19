package com.example.avialine.model.client;

import com.example.avialine.messages.ApiErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RequiredArgsConstructor
@Component
public class SirenaClient {

    @Value("${sirena.host}")
    private final String host;

    @Value("${sirena.port}")
    private final int port;

    @Value("${sirena.timeout}")
    private final int timeout;

    public String sendRequest(String xml) throws IOException {
        byte[] xmlBytes = xml.getBytes(StandardCharsets.UTF_8);
        byte[] compressed = compress(xmlBytes);

        byte[] header = buildHeader(compressed.length);
        byte[] payload = concat(header, compressed);

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeout * 1000);
            socket.setSoTimeout(timeout * 1000);

            socket.getOutputStream().write(payload);
            socket.getOutputStream().flush();

            byte[] respHeader = socket.getInputStream().readNBytes(100);
            int respLength = ByteBuffer.wrap(respHeader)
                    .order(ByteOrder.BIG_ENDIAN).getInt(0);

            byte[] respData = socket.getInputStream().readNBytes(respLength);
            return new String(decompress(respData), StandardCharsets.UTF_8);
        }
    }

    private byte[] buildHeader(int dataLength) {
        byte[] header = new byte[100];
        ByteBuffer buffer = ByteBuffer.wrap(header).order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(0, dataLength);
        buffer.putInt(4, (int) (System.currentTimeMillis() / 1000));
        buffer.putInt(8, 12345);
        buffer.putShort(44, (short) 5149);
        header[46] = 0x04;

        return header;
    }

    private byte[] compress(byte[] data) throws IOException {
        Deflater deflater = new Deflater(Deflater.NO_COMPRESSION, false);
        deflater.setInput(data);
        deflater.finish();

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            byte[] buffer = new byte[1024];

            while (!deflater.finished()) {
                out.write(buffer, 0, deflater.deflate(buffer));
            }

            return out.toByteArray();
        }finally {
            deflater.end();
        }
    }

    private byte[] decompress(byte[] data) throws IOException {
        Inflater inflater = new Inflater(false);
        inflater.setInput(data);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];

            while (!inflater.finished()) {

                try {

                    out.write(buffer,0, inflater.inflate(buffer));

                }catch (DataFormatException e){

                    throw new IOException(ApiErrorMessage.DECOMPRESS_ERROR_MESSAGE.getMessage());

                }

            }

            return out.toByteArray();

        }finally {
            inflater.end();
        }

    }

    private byte[] concat(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];

        /*
         the 'a' source of income,
         '0' is the start index we start copy,
         'result' is the array whe input there,
         and the second '0' is the start position we start input the data,
         'a.length' the num of elements we copy!
         */
        System.arraycopy(a , 0, result, 0, a.length); // the 'a' source of income,
        System.arraycopy(b, 0, result, a.length, b.length);

        return result;
    }
}
