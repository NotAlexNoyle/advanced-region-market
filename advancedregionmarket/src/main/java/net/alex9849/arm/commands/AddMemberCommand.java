package net.alex9849.arm.commands;

import net.alex9849.arm.AdvancedRegionMarket;
import net.alex9849.arm.Handler.CommandHandler;
import net.alex9849.arm.Messages;
import net.alex9849.arm.Permission;
import net.alex9849.arm.exceptions.InputException;
import net.alex9849.arm.minifeatures.PlayerRegionRelationship;
import net.alex9849.arm.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AddMemberCommand extends BasicArmCommand {

    private final String rootCommand = "addmember";
    private final String regex = "(?i)addmember [^;\n ]+ [^;\n ]+";
    private final String usage = "/arm addmember [REGION] [NEWMEMBER]";

    @Override
    public boolean matchesRegex(String command) {
        return command.matches(this.regex);
    }

    @Override
    public String getRootCommand() {
        return this.rootCommand;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean runCommand(CommandSender sender, Command cmd, String commandsLabel, String[] args, String allargs) throws InputException {
        if (!(sender instanceof Player)) {
            throw new InputException(sender, Messages.COMMAND_ONLY_INGAME);
        }
        Region region = Region.searchRegionbyNameAndWorld(args[1], ((Player) sender).getWorld().getName());
        if(region == null){
            throw new InputException(sender, Messages.REGION_DOES_NOT_EXIST);
        }

        Player playermember = Bukkit.getPlayer(args[2]);
        if(playermember == null) {
            throw new InputException(sender, Messages.REGION_ADD_MEMBER_NOT_ONLINE);
        }
        if(AdvancedRegionMarket.getWorldGuardInterface().hasOwner((Player) sender, region.getRegion()) && sender.hasPermission(Permission.MEMBER_ADDMEMBER)) {
            AdvancedRegionMarket.getWorldGuardInterface().addMember(playermember, region.getRegion());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);
        } else if (sender.hasPermission(Permission.ADMIN_ADDMEMBER)){
            AdvancedRegionMarket.getWorldGuardInterface().addMember(playermember, region.getRegion());
            sender.sendMessage(Messages.PREFIX + Messages.REGION_ADD_MEMBER_ADDED);
        } else if (!(sender.hasPermission(Permission.MEMBER_ADDMEMBER))){
            throw new InputException(sender, Messages.NO_PERMISSION);
        } else {
            throw new InputException(sender, Messages.REGION_ADD_MEMBER_DO_NOT_OWN);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(Player player, String[] args) {
        List<String> returnme = new ArrayList<>();

        if(args.length >= 1) {
            if (this.rootCommand.startsWith(args[0])) {
                if (player.hasPermission(Permission.ADMIN_ADDMEMBER) || player.hasPermission(Permission.MEMBER_ADDMEMBER)) {
                    if(args.length == 1) {
                        returnme.add(this.rootCommand);
                    } else if(args.length == 2) {
                        PlayerRegionRelationship playerRegionRelationship = null;
                        if(player.hasPermission(Permission.ADMIN_ADDMEMBER)) {
                            playerRegionRelationship = PlayerRegionRelationship.ALL;
                        } else {
                            playerRegionRelationship = PlayerRegionRelationship.OWNER;
                        }
                        returnme.addAll(Region.completeTabRegions(player, args[1], playerRegionRelationship));
                    } else if(args.length == 3) {
                        returnme.addAll(CommandHandler.tabCompleteOnlinePlayers(args[2]));
                    }
                }
            }
        }
        return returnme;
    }
}
