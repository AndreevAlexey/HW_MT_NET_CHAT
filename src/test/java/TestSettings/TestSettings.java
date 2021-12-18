package TestSettings;

import Service.Settings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

public class TestSettings {
    final Settings settings = new Settings();
    String dir;
    String fileName;

    @BeforeEach
    public void init() {
        dir = "C://JAVA//NET_CHAT//TEST//";
        fileName = "settings"+System.currentTimeMillis()+".txt";
    }

    @Test
    public void Test_save_not_exist_dir() {
        // given
        dir = dir + "setting_" + System.currentTimeMillis();
        File newDir = new File(dir);
        boolean expected = true;
        boolean result = false;
        // when
        settings.save(dir, fileName);
        result = newDir.exists();
        // then
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void Test_save() {
        // given
        File newFile = new File(dir, fileName);
        boolean expected = true;
        boolean result = false;
        // when
        result = settings.save(dir, fileName);
        // then
        Assertions.assertEquals(expected, result);
        Assertions.assertEquals(expected, newFile.exists());
    }

    @Test
    public void Test_load_not_exist_dir() {
        // given
        dir = dir + "load";
        Settings result = null;
        // when
        result = Settings.load(dir, fileName);
        // then
        Assertions.assertNull(result);
    }
    @Test
    public void Test_load_not_exist_file() {
        // given
        fileName = "new_" + fileName;
        Settings result = null;
        // when
        result = Settings.load(dir, fileName);
        // then
        Assertions.assertNull(result);
    }

    @Test
    public void Test_load() {
        // given
        settings.save(dir, fileName);
        Settings result = null;
        // when
        result = Settings.load(dir, fileName);
        // then
        Assertions.assertNotNull(result);
    }

}
