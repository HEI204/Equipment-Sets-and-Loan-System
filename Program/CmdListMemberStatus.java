public class CmdListMemberStatus implements Command{
    
    @Override
    public void execute(String[] cmdParts) {
        Club.getInstance().listClubMemberStatus();
    }
}
