package utils;

import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
public final class ScreenshotBus {

    // El búfer vive por hilo de prueba; se limpia después de cada paso
    private static final ThreadLocal<List<Shot>> TL =
            ThreadLocal.withInitial(ArrayList::new);

    public static void push(byte[] bytes, String description) {
        TL.get().add(new Shot(bytes, description));
    }

    public static List<Shot> drain() {
        List<Shot> list = new ArrayList<>(TL.get());
        TL.get().clear();
        return list;
    }

    // Contenedor de valor inmutable
    public record Shot(byte[] bytes, String description) {
    }
}
