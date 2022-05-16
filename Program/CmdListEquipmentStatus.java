public class CmdListEquipmentStatus implements Command {

    @Override
    public void execute(String[] cmdParts) {
        Club.getInstance().listClubEquipmentStatus();
    }
}