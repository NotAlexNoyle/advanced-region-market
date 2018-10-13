package net.alex9849.arm.Preseter;

import net.alex9849.arm.Messages;
import net.alex9849.arm.regions.RegionKind;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class Preset {
    private static YamlConfiguration config;
    protected Player assignedPlayer;
    protected boolean hasPrice = false;
    protected double price = 0;
    protected boolean hasRegionKind = false;
    protected RegionKind regionKind = RegionKind.DEFAULT;
    protected boolean hasAutoReset = false;
    protected boolean autoReset = true;
    protected boolean hasIsHotel = false;
    protected boolean isHotel = false;
    protected boolean hasDoBlockReset = false;
    protected boolean doBlockReset = true;
    protected String name = "default";

    public Preset(Player player){
        assignedPlayer = player;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPlayer(Player player){
        this.assignedPlayer = player;
    }

    public Player getAssignedPlayer(){
        return this.assignedPlayer;
    }

    public void setPrice(double price){
        if(price < 0){
            price = price * (-1);
        }
        this.hasPrice = true;
        this.price = price;
    }

    public abstract void getPresetInfo(Player player);

    public void removePrice(){
        this.hasPrice = false;
        this.price = 0;
    }

    public boolean hasPrice() {
        return hasPrice;
    }

    public boolean hasRegionKind() {
        return hasRegionKind;
    }

    public boolean hasAutoReset() {
        return hasAutoReset;
    }

    public boolean hasIsHotel() {
        return hasIsHotel;
    }

    public boolean isHasDoBlockReset() {
        return hasDoBlockReset;
    }

    public void setRegionKind(RegionKind regionKind){
        if(regionKind == null) {
            regionKind = RegionKind.DEFAULT;
        }
        this.hasRegionKind = true;
        this.regionKind = regionKind;
    }

    public void setDoBlockReset(Boolean bool){
        this.hasDoBlockReset = true;
        this.doBlockReset = bool;
    }

    public void removeDoBlockReset(){
        this.hasDoBlockReset = false;
        this.doBlockReset = true;
    }

    public void removeRegionKind(){
        this.hasRegionKind = false;
        this.regionKind = RegionKind.DEFAULT;
    }

    public void setAutoReset(Boolean autoReset) {
        this.hasAutoReset = true;
        this.autoReset = autoReset;
    }

    public void removeAutoReset(){
        this.hasAutoReset = false;
        this.autoReset = true;
    }

    public void setHotel(Boolean isHotel){
        this.hasIsHotel = true;
        this.isHotel = isHotel;
    }

    public void removeHotel(){
        this.hasIsHotel = false;
        this.isHotel = false;
    }

    public double getPrice() {
        return price;
    }

    public RegionKind getRegionKind() {
        return regionKind;
    }

    public boolean isAutoReset() {
        return autoReset;
    }

    public boolean isDoBlockReset() {
        return doBlockReset;
    }

    public boolean isHotel() {
        return isHotel;
    }

    public static YamlConfiguration getConfig(){
        return Preset.config;
    }

    public static void generatedefaultConfig(){
        Plugin plugin = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket");
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File messagesdic = new File(pluginfolder + "/presets.yml");
        if(!messagesdic.exists()){
            try {
                InputStream stream = plugin.getResource("presets.yml");
                byte[] buffer = new byte[stream.available()];
                stream.read(buffer);
                OutputStream output = new FileOutputStream(messagesdic);
                output.write(buffer);
                output.close();
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadConfig(){
        Messages.generatedefaultConfig();
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File presetsconfigdic = new File(pluginfolder + "/presets.yml");
        Preset.config = YamlConfiguration.loadConfiguration(presetsconfigdic);
    }

    public static void saveRegionsConf(YamlConfiguration conf) {
        File pluginfolder = Bukkit.getPluginManager().getPlugin("AdvancedRegionMarket").getDataFolder();
        File regionsconfigdic = new File(pluginfolder + "/presets.yml");
        try {
            conf.save(regionsconfigdic);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> onTabCompleteCompleteSavedPresets(String args[]) {
        List<String> returnme = new ArrayList<>();
        if(args[0].equalsIgnoreCase("sellpreset")) {
            for(SellPreset preset: SellPreset.getPatterns()) {
                if(preset.getName().toLowerCase().startsWith(args[2])) {
                    returnme.add(preset.getName());
                }
            }
        }
        if(args[0].equalsIgnoreCase("rentpreset")) {
            for(RentPreset preset: RentPreset.getPatterns()) {
                if(preset.getName().toLowerCase().startsWith(args[2])) {
                    returnme.add(preset.getName());
                }
            }
        }
        if(args[0].equalsIgnoreCase("contractpreset")) {
            for(ContractPreset preset: ContractPreset.getPatterns()) {
                if(preset.getName().toLowerCase().startsWith(args[2])) {
                    returnme.add(preset.getName());
                }
            }
        }
        return returnme;
    }
}
