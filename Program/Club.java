import java.util.ArrayList;
import java.util.Collections;

public class Club {
    private ArrayList<Member> allMembers;
    private ArrayList<Equipment> allEqiupments;
    private static Club instance = new Club();

    private Club() {
        allMembers = new ArrayList<>();
        allEqiupments = new ArrayList<>();
    }

    public static Club getInstance() {
        return instance;
    }

    public void addMember(Member m) {
        allMembers.add(m);
        Collections.sort(allMembers);
    }

    public void removeMember(Member m) {
        allMembers.remove(m);
    }

    public void addEqiupment(Equipment e) {
        allEqiupments.add(e);
        Collections.sort(allEqiupments);
    }

    public void removeEquipment(Equipment e) {
        allEqiupments.remove(e);
    }

    public void listClubMembers() {
        System.out.println(Member.getListingHeader());
        for (Member m : allMembers)
            System.out.println(m);
    }

    public void listClubEquipment() {
        System.out.println(Equipment.getListingHeader());
        for (Equipment e : allEqiupments)
            System.out.println(e);
    }

    public void listClubMemberStatus() {
        for (Member m : allMembers) {
            System.out.println("[" + m.getID() + " " + m.getName() + "]");
            System.out.println(m.getStatus());
        }
    }

    public void listClubEquipmentStatus() {
        for (Equipment e : allEqiupments) {
            System.out.println("[" + e.getEquipmentCode() + " " + e.getEquipmentName() + "]");
            System.out.println(e.getEquipmentStatus());
        }
    }

    public Member checkMemberExistById(String newMemberID) {
        for (Member m : allMembers)
            if (m.checkIdInUse(newMemberID))
                return m;
        return null;
    }

    public Equipment checkEquipmentExistByCode(String newEquipmemntCode) {
        for (Equipment e : allEqiupments)
            if (e.checkCodeInUse(newEquipmemntCode))
                return e;
        return null;
    }

}
