package net.ledestudio.loomify;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.ledestudio.loomify.io.Lson;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.UUID;

public class LsonTest {

    private record LsonTestData(String id, String name) {}

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void dataSaveTest() {
        Lson lson = new Lson(gson);
        lson.save(Path.of("data/test.json"), new LsonTestData("id_test", "name_test"));
        System.out.println("Lson data save success.");
    }

    @Test
    public void dataLoadTest() {
        Lson lson = new Lson(gson);
        LsonTestData data = lson.load(Path.of("data/test.json"), LsonTestData.class);
        System.out.println("Lson data load success.");

        if (data == null) {
            System.out.println("Loaded data is null.");
        } else {
            System.out.println(data);
        }
    }

    @Test
    public void dataDeleteTest() {
        Lson lson = new Lson(gson);
        lson.delete(Path.of("data/test.json"));
        System.out.println("Lson data delete success.");
    }

    @Test
    public void dataDirDeleteTest() {
        Lson lson = new Lson(gson);
        lson.delete(Path.of("data"));
        System.out.println("Lson data dir delete success.");
    }

    @Test
    public void checkSaveMilliseconds() {
        long start = System.currentTimeMillis();
        Lson lson = new Lson(gson);
        for (int i = 0; i < 1000; i++) {
            lson.save(Path.of("data/test_" + i + ".json"), new LsonTestData("id_test", "name_test"));
        }

        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println("Save Time Elapsed: " + timeElapsed + "ms");
    }

    @Test
    public void checkLoadMilliseconds() {
        long start = System.currentTimeMillis();
        Lson lson = new Lson(gson);
        for (int i = 0; i < 1000; i++) {
            lson.load(Path.of("data/test_" + i + ".json"), LsonTestData.class);
        }

        long timeElapsed = System.currentTimeMillis() - start;
        System.out.println("Load Time Elapsed: " + timeElapsed + "ms");
    }

    @Test
    public void checkDeleteMilliseconds() {
        Lson lson = new Lson(gson);

        String dirRoot = UUID.randomUUID().toString();
        String dirSubFirst = UUID.randomUUID().toString();
        String dirSubSecond = UUID.randomUUID().toString();

        // Create data
        long startCreate = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            lson.save(Path.of(dirRoot + "/test" + i + ".json"), new LsonTestData("id", "name"));
        }

        for (int i = 0; i < 1000; i++) {
            lson.save(Path.of(dirRoot + "/" + dirSubFirst + "/test" + i + ".json"), new LsonTestData("id", "name"));
        }

        for (int i = 0; i < 1000; i++) {
            lson.save(Path.of(dirRoot + "/" + dirSubSecond + "/test" + i + ".json"), new LsonTestData("id", "name"));
        }

        long createTimeElapsed = System.currentTimeMillis() - startCreate;
        System.out.println("Create Time Elapsed: " + createTimeElapsed + "ms");


        // Delete data
        long startDelete = System.currentTimeMillis();
        lson.delete(Path.of(dirRoot));

        long deleteTimeElapsed = System.currentTimeMillis() - startDelete;
        System.out.println("Delete Time Elapsed: " + deleteTimeElapsed + "ms");
    }

}
