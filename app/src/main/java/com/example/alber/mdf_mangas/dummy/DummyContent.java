package com.example.alber.mdf_mangas.dummy;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    private static String url_consulta;
    private static JSONArray jSONArray;
    private static DevuelveJSON devuelveJSON;

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    /*static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }*/

    static {
        url_consulta = "http://iesayala.ddns.net/Albert/Generos.php";
        devuelveJSON = new DevuelveJSON();
        new ListaGenero().execute();
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /*private static DummyItem createDummyItem(int position) {
        return new DummyItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }*/

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String nombre;
        public final String autor;
        public final String editorial;
        public final String genero;
        public final String tomos;
        public final String sipnosis;

        public DummyItem(String id, String nombre, String autor, String editorial, String genero, String tomos, String sipnosis) {
            this.id = id;
            this.nombre = nombre;
            this.autor = autor;
            this.editorial = editorial;
            this.genero = genero;
            this.tomos = tomos;
            this.sipnosis = sipnosis;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    static class ListaGenero extends AsyncTask<String, String, JSONArray> {

        @Override
        protected void onPreExecute() {}

        @Override
        protected JSONArray doInBackground(String... args) {
            try {
                HashMap<String, String> parametrosPost = new HashMap<>();
                parametrosPost.put("ins_sql", "Select * from Manga");
                jSONArray = devuelveJSON.sendRequest(url_consulta, parametrosPost);
                System.out.println(jSONArray);

                if (jSONArray != null) {
                    return jSONArray;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONArray json) {
            if (json != null) {
                for (int i = 0; i < json.length(); i++) {
                    try {
                        JSONObject jsonObject = json.getJSONObject(i);
                        addItem(new DummyItem(jsonObject.getString("IdManga"),jsonObject.getString("Nombre"), jsonObject.getString("Autor"), jsonObject.getString("Genero"), jsonObject.getString("Editorial"), jsonObject.getString("Tomos"), jsonObject.getString("Sipnosis")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    public static class DevuelveJSON {
        private HttpURLConnection conn;
        public static final int CONNECTION_TIMEOUT = 15 * 1000;
        public JSONArray sendRequest(String link, HashMap<String, String>
                values)
                throws JSONException {
            JSONArray jArray = null;
            try {
                URL url = new URL(link);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(CONNECTION_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.connect();
                if (values != null) {
                    OutputStream os = conn.getOutputStream();
                    OutputStreamWriter osWriter = new OutputStreamWriter(os,
                            "UTF-8");
                    BufferedWriter writer = new BufferedWriter(osWriter);
                    writer.write(getPostData(values));
                    writer.flush();
                    writer.close();
                    os.close();
                }
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader isReader = new InputStreamReader(is,
                            "UTF-8");
                    BufferedReader reader = new BufferedReader(isReader);
                    String result = "";
                    String line = null;
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    result=sb.toString();
                    try{
                        jArray = new JSONArray(result);
                        return jArray;
                    } catch(JSONException e){
                        Log.e("ERROR => ", "Error convirtiendo los datos a JSON : " + e.toString());
                        e.printStackTrace();
                        return null;
                    } catch (Exception e) {}
                }
            }
            catch (MalformedURLException e) {}
            catch (IOException e) {}
            return jArray;
        }
        public String getPostData(HashMap<String, String> values) {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            try {
                for (Map.Entry<String, String> entry : values.entrySet()) {
                    if (first)
                        first = false;
                    else
                        builder.append("&");
                    try {
                        builder.append(URLEncoder.encode(entry.getKey(),
                                "UTF-8"));
                        builder.append("=");
                        builder.append(URLEncoder.encode(entry.getValue(),
                                "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            } catch (Exception e){}
            return builder.toString();
        }
    }
}
