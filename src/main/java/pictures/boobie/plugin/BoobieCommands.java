package pictures.boobie.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BoobieCommands implements CommandExecutor {
    
    private BoobiePlugin plugin;
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        
        switch (cmd.getName()) {
            case "search":
                return commandSearch(sender, args);
        }
        
        return false;
    }
    
    public boolean commandSearch(CommandSender sender, String[] args) {
        return true;
    }
    
}
