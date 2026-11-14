package com.example.staffdetector;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class StaffDetectorAddon extends MeteorAddon {
    public static final Category CATEGORY = new Category("Staff");

    @Override
    public void onInitialize() {
        Modules.get().add(new StaffDetector());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.example.staffdetector";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("forminecrafta76-oss", "staff-detector");
    }
}
