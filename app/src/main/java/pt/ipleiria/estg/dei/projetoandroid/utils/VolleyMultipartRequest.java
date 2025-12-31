// VolleyMultipartRequest.java
package pt.ipleiria.estg.dei.projetoandroid.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {

    private static final String LINE_FEED = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "volleyBoundary";

    private final Response.Listener<NetworkResponse> listener;
    private final Map<String, String> stringParams;
    private final Map<String, DataPart> fileParams;

    public VolleyMultipartRequest(
            int method,
            String url,
            Map<String, String> stringParams,
            Map<String, DataPart> fileParams,
            Response.Listener<NetworkResponse> listener,
            Response.ErrorListener errorListener) {

        super(method, url, errorListener);
        this.listener = listener;
        this.stringParams = stringParams;
        this.fileParams = fileParams;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + BOUNDARY;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return super.getHeaders();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            // Campos de texto (animal_id, etc.)
            if (stringParams != null) {
                for (Map.Entry<String, String> entry : stringParams.entrySet()) {
                    bos.write((TWO_HYPHENS + BOUNDARY + LINE_FEED).getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" +
                            entry.getKey() + "\"" + LINE_FEED).getBytes());
                    bos.write(LINE_FEED.getBytes());
                    bos.write(entry.getValue().getBytes());
                    bos.write(LINE_FEED.getBytes());
                }
            }

            // Ficheiros
            if (fileParams != null) {
                for (Map.Entry<String, DataPart> entry : fileParams.entrySet()) {

                    DataPart dataPart = entry.getValue();

                    bos.write((TWO_HYPHENS + BOUNDARY + LINE_FEED).getBytes());
                    bos.write(("Content-Disposition: form-data; name=\"" +
                            entry.getKey() +
                            "\"; filename=\"" +
                            dataPart.getFileName() +
                            "\"" + LINE_FEED).getBytes());

                    if (dataPart.getType() != null) {
                        bos.write(("Content-Type: " + dataPart.getType() + LINE_FEED).getBytes());
                    }

                    bos.write(LINE_FEED.getBytes());
                    bos.write(dataPart.getContent());
                    bos.write(LINE_FEED.getBytes());
                }
            }

            bos.write((TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_FEED).getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return bos.toByteArray();
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        listener.onResponse(response);
    }

    // Classe interna DataPart
    public static class DataPart {
        private final String fileName;
        private final byte[] content;
        private final String type;

        public DataPart(String fileName, byte[] content, String type) {
            this.fileName = fileName;
            this.content = content;
            this.type = type;
        }

        public String getFileName() {
            return fileName;
        }

        public byte[] getContent() {
            return content;
        }

        public String getType() {
            return type;
        }
    }
}









//package pt.ipleiria.estg.dei.projetoandroid.utils;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NetworkResponse;
//import com.android.volley.Request;
//import com.android.volley.Response;
//import com.android.volley.toolbox.HttpHeaderParser;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.Map;
//
//public class VolleyMultipartRequest extends Request<NetworkResponse> {
//
//    private static final String LINE_FEED = "\r\n";
//    private static final String TWO_HYPHENS = "--";
//    private static final String BOUNDARY = "volleyBoundary";
//
//    private final Response.Listener<NetworkResponse> listener;
//    private final Map<String, String> headers;
//    private final Map<String, DataPart> params;
//
//    public VolleyMultipartRequest(
//            int method,
//            String url,
//            Map<String, String> headers,
//            Map<String, DataPart> params,
//            Response.Listener<NetworkResponse> listener,
//            Response.ErrorListener errorListener) {
//
//        super(method, url, errorListener);
//        this.listener = listener;
//        this.headers = headers;
//        this.params = params;
//    }
//
//    @Override
//    public String getBodyContentType() {
//        return "multipart/form-data;boundary=" + BOUNDARY;
//    }
//
//    @Override
//    public Map<String, String> getHeaders() throws AuthFailureError {
//        return headers != null ? headers : super.getHeaders();
//    }
//
//    @Override
//    public byte[] getBody() throws AuthFailureError {
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//
//        try {
//            if (params != null) {
//                for (Map.Entry<String, DataPart> entry : params.entrySet()) {
//
//                    DataPart dataPart = entry.getValue();
//
//                    bos.write((TWO_HYPHENS + BOUNDARY + LINE_FEED).getBytes());
//                    bos.write(("Content-Disposition: form-data; name=\"" +
//                            entry.getKey() +
//                            "\"; filename=\"" +
//                            dataPart.getFileName() +
//                            "\"" + LINE_FEED).getBytes());
//
//                    if (dataPart.getType() != null) {
//                        bos.write(("Content-Type: " + dataPart.getType() + LINE_FEED).getBytes());
//                    }
//
//                    bos.write(LINE_FEED.getBytes());
//                    bos.write(dataPart.getContent());
//                    bos.write(LINE_FEED.getBytes());
//                }
//            }
//
//            bos.write((TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_FEED).getBytes());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return bos.toByteArray();
//    }
//
//    @Override
//    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
//        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
//    }
//
//    @Override
//    protected void deliverResponse(NetworkResponse response) {
//        listener.onResponse(response);
//    }
//
//    // 🔹 Classe interna DataPart
//    public static class DataPart {
//        private final String fileName;
//        private final byte[] content;
//        private final String type;
//
//        public DataPart(String fileName, byte[] content, String type) {
//            this.fileName = fileName;
//            this.content = content;
//            this.type = type;
//        }
//
//        public String getFileName() {
//            return fileName;
//        }
//
//        public byte[] getContent() {
//            return content;
//        }
//
//        public String getType() {
//            return type;
//        }
//    }
//}
