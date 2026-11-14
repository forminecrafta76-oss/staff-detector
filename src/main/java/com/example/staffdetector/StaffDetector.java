package com.example.staffdetector;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class StaffDetector extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> staffNames = sgGeneral.add(new StringListSetting.Builder()
        .name("staff-names")
        .description("Nama pemain yang dianggap staff/admin.")
        .defaultValue(List.of())
        .build()
    );

    private final Setting<Boolean> notifyLeave = sgGeneral.add(new BoolSetting.Builder()
        .name("notify-leave")
        .description("Kasih tahu juga saat staff keluar dari server.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> disconnect = sgGeneral.add(new BoolSetting.Builder()
        .name("disconnect")
        .description("Otomatis keluar dari server saat staff terdeteksi.")
        .defaultValue(false)
        .build()
    );

    private final Set<String> present = new HashSet<>();

    public StaffDetector() {
        super(StaffDetectorAddon.CATEGORY, "staff-detector", "Mendeteksi staff/admin dari daftar nama yang kamu isi.");
    }

    @Override
    public void onActivate() {
        present.clear();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (mc.getNetworkHandler() == null) return;

        Set<String> targets = new HashSet<>();
        for (String s : staffNames.get()) targets.add(s.toLowerCase(Locale.ROOT));
        if (targets.isEmpty()) return;

        Set<String> now = new HashSet<>();
        for (PlayerListEntry entry : mc.getNetworkHandler().getPlayerList()) {
            String name = entry.getProfile().name();
            if (targets.contains(name.toLowerCase(Locale.ROOT))) now.add(name);
        }

        for (String name : now) {
            if (present.add(name)) {
                warning("Staff terdeteksi: (highlight)%s(default).", name);
                if (disconnect.get()) {
                    mc.getNetworkHandler().getConnection().disconnect(Text.literal("Staff terdeteksi: " + name));
                    toggle();
                    return;
                }
            }
        }

        if (notifyLeave.get()) {
            for (String name : new HashSet<>(present)) {
                if (!now.contains(name)) info("Staff keluar: (highlight)%s(default).", name);
            }
        }
        present.retainAll(now);
    }
          }
