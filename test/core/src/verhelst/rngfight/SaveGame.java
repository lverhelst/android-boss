package verhelst.rngfight;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import verhelst.CustomActors.Character;

/**
 * Created by Orion on 12/16/2014.
 */
public class SaveGame extends DefaultHandler {

    private static verhelst.CustomActors.Character leftside, rightside;

    public static int[] stats;
    public static int[][] unclocks;
    public static int[] weapon_unlocks;

    private int charind;

    private enum States {
        OTHER, STATS, CHAR1, CHAR2, UNCLOCKS
    }

    private States state;
    private boolean shouldequip;

    private String currentNode = "xml";
    private Weapon a, b;

    private static RngFight fight;
    private static Preferences pref;

    public SaveGame(RngFight fight1) throws Exception {
        state = States.OTHER;
        this.fight = fight1;
    }

    public void readGameSave() throws Exception {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser newSAXParser = saxParserFactory.newSAXParser();
        XMLReader xr = newSAXParser.getXMLReader();
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            xr = XMLReaderFactory.createXMLReader();

        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        // FileHandle fh = Gdx.files.internal("save.xml");

        pref = Gdx.app.getPreferences("save");
        String data = pref.getString("save");

        if (data == null || data.equalsIgnoreCase("")) {
            FileHandle fh = Gdx.files.internal("defaults.xml");
            data = fh.readString();
            pref.putString("save", data);
            pref.flush();
        }
        charind = 0;
        xr.parse(new InputSource(new ByteArrayInputStream(data.getBytes())));


        System.out.println("Break here");
    }

    public static void saveGame(Character A, Character B, int[] stats) {
        int indexForLevel = Math.min(A.getLevel() / 5, Assets.faces.length - 1);

        String unclocksString = Assets.unclocks.length + ";" + Assets.unclocks[0].length + ";";
        for (int i = 0; i < Assets.unclocks.length; i++) {
            for (int j = 0; j < Assets.unclocks[i].length; j++) {
                unclocksString += Assets.unclocks[i][j];
            }
        }
        String weaponsUnclocksString = Assets.weaponUnlocks.length + ";";
        for(int i = 0; i < Assets.weaponUnlocks.length; i++){
            weaponsUnclocksString += Assets.weaponUnlocks[i];
        }

        String s = "<?xml version=\"1.0\"?>" +
                "<Data>" +
                    "<Stats>" +
                        "<MaxHits>" + stats[0] + "</MaxHits>" +
                        "<MinHits>" + stats[1] + "</MinHits>" +
                        "<player2wins>" + stats[2] + "</player2wins>" +
                        "<player2losses>" + stats[3] + "</player2losses>" +
                        "<draws>" + stats[4] + "</draws>" +
                        "<games>" + stats[5] + "</games>" +
                        "<max_level_reached>" + stats[6] + "</max_level_reached>" +
                    "</Stats>" +
                    "<Character>" +
                        "<index>1</index>" +
                        "<Name>" + A.getName() + "</Name>" +
                        "<Level>" + A.getLevel() + "</Level>" +
                        "<Max_Level>" + A.getMax_level() + "</Max_Level>" +
                        "<Max_WTNL>" + A.getMax_wtnl() + "</Max_WTNL>" +
                        "<WinStreak>" + A.getWin_streak() + "</WinStreak>" +
                        "<LoseStreak>" + A.getLose_streak() + "</LoseStreak>" +
                        "<Head>" + indexForLevel + "</Head>" +
                        "<Torso>" + indexForLevel + "</Torso>" +
                        "<Legs>" + indexForLevel + "</Legs>" +
                        "<Shoulder>" + indexForLevel + "</Shoulder>" +
                        "<Elbow>" + indexForLevel + "</Elbow>" +
                        "<Weapon>" +
                            "<SpriteNumber>" + (A.getEquipped_weapon() == null ? -1 : A.getEquipped_weapon().spriteindex) + "</SpriteNumber>" +
                            "<MaxDamage>" + (A.getEquipped_weapon() == null ? 0 : A.getEquipped_weapon().getMax_damage()) + "</MaxDamage>" +
                            "<MinDamage>" + (A.getEquipped_weapon() == null ? 0 : A.getEquipped_weapon().getMin_damage()) + "</MinDamage>" +
                            "<Hearts>" + (A.getEquipped_weapon() == null ? 1 : A.getEquipped_weapon().getHp_multiplier()) + "</Hearts>" +
                        "</Weapon>" +
                    "</Character>" +
                    "<Character>" +
                        "<index>2</index>" +
                        "<Name>" + B.getName() + "</Name>" +
                        "<Level>" + B.getLevel() + "</Level>" +
                        "<Max_Level>" + B.getMax_level() + "</Max_Level>" +
                        "<Max_WTNL>" + B.getMax_wtnl() + "</Max_WTNL>" +
                        "<WinStreak>" + B.getWin_streak() + "</WinStreak>" +
                        "<LoseStreak>" + B.getLose_streak() + "</LoseStreak>" +
                        "<Head>" + B.getSpriteindices()[0] + "</Head>" +
                        "<Torso>" + B.getSpriteindices()[1] + "</Torso>" +
                        "<Legs>" + B.getSpriteindices()[2] + "</Legs>" +
                        "<Shoulder>" + B.getSpriteindices()[3] + "</Shoulder>" +
                        "<Elbow>" + B.getSpriteindices()[4] + "</Elbow>" +
                        "<Weapon>" +
                            "<SpriteNumber>" + (B.getEquipped_weapon() == null ? -1 : B.getEquipped_weapon().spriteindex) + "</SpriteNumber>" +
                            "<MaxDamage>" + (B.getEquipped_weapon() == null ? 0 : B.getEquipped_weapon().getMax_damage()) + "</MaxDamage>" +
                            "<MinDamage>" + (B.getEquipped_weapon() == null ? 0 : B.getEquipped_weapon().getMin_damage()) + "</MinDamage>" +
                            "<Hearts>" + (B.getEquipped_weapon() == null ? 1 : B.getEquipped_weapon().getHp_multiplier()) + "</Hearts>" +
                        "</Weapon>" +
                    "</Character>" +
                    "<Unclocks>" +
                        "<WeaponUnlocks>" + weaponsUnclocksString + "</WeaponUnlocks>"+
                        "<GearUnlocks>" + unclocksString + "</GearUnlocks>" +
                    "</Unclocks>" +
                "</Data>";
        // FileHandle fh = Gdx.files.local("save.xml");
        // fh.writeString(s, false);
        pref.putString("save", s);
        pref.flush();
    }

    public static void reset() {
        FileHandle fh = Gdx.files.internal("defaults.xml");
        // FileHandle fh2 = Gdx.files.local("save.xml");
        // fh2.writeString(fh.readString(), false);
        pref.putString("save", fh.readString());
        pref.flush();
        fight.reload();
    }


    /**
     * * Handle XML document thing ****
     */
    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
       // System.out.print(start + " " + length + "");
        String st = "";
        for (int i = start; i < start + length; i++) {
            st += ch[i];//System.out.println((i - start) + "  " + ch[i]);
        }
        loadDatum(st);
        super.characters(ch, start, length);
    }

    private void loadDatum(String str) {
        switch (state) {
            case STATS:
                if (currentNode.equalsIgnoreCase("maxhits")) {
                    stats[0] = Integer.parseInt(str);
                }
                if (currentNode.equalsIgnoreCase("minhits")) {
                    stats[1] = Integer.parseInt(str);
                }
                if (currentNode.equalsIgnoreCase("player2wins")) {
                    stats[2] = Integer.parseInt(str);
                }
                if (currentNode.equalsIgnoreCase("player2losses")) {
                    stats[3] = Integer.parseInt(str);
                }
                if (currentNode.equalsIgnoreCase("draws")) {
                    stats[4] = Integer.parseInt(str);
                }
                if (currentNode.equalsIgnoreCase("games")) {
                    stats[5] = Integer.parseInt(str);
                }
                if (currentNode.equalsIgnoreCase("max_level_reached")) {
                    stats[6] = Integer.parseInt(str);
                }

                break;
            case CHAR1:
                if (currentNode.equalsIgnoreCase("name")) {
                    leftside = new Character(str);
                }
                if (currentNode.equalsIgnoreCase("level")) {
                    leftside.setLevel(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("max_level")) {
                    leftside.setMax_level(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("max_wtnl")) {
                    leftside.setMax_wtnl(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("winstreak")) {
                    leftside.setWin_streak(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("losestreak")) {
                    leftside.setLose_streak(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("head")) {
                    leftside.setBodyPart("head", Assets.faces[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("shoulder")) {
                    leftside.setBodyPart("shoulder", Assets.shoulders[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("legs")) {
                    leftside.setBodyPart("leg", Assets.pants[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("torso")) {
                    leftside.setBodyPart("torso", Assets.shirts[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("elbow")) {
                    leftside.setBodyPart("elbow", Assets.arms[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("weapon")) {
                    System.out.println("NEW WEP");
                    a = new Weapon();
                }
                if (currentNode.equalsIgnoreCase("spritenumber")) {
                    int spritenum = Integer.parseInt(str);
                    if(a == null)
                        a = new Weapon();
                    if (spritenum != -1) {
                        shouldequip = true;
                        a.setSprite(Assets.weapons_sprites.get(spritenum));
                    } else {
                        shouldequip = false;
                    }
                }
                if (currentNode.equalsIgnoreCase("mindamage")) {
                    a.setMin_damage(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("maxdamage")) {
                    a.setMax_damage(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("hearts")) {
                    a.setHp_multiplier(Integer.parseInt(str));
                    if (shouldequip) {
                        leftside.setEquipped_weapon(a);
                    }
                }

                break;
            case CHAR2:
                if (currentNode.equalsIgnoreCase("name")) {
                    rightside = new Character(str);
                }
                if (currentNode.equalsIgnoreCase("level")) {
                    rightside.setLevel(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("max_level")) {
                    rightside.setMax_level(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("max_wtnl")) {
                    rightside.setMax_wtnl(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("winstreak")) {
                    rightside.setWin_streak(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("losestreak")) {
                    rightside.setLose_streak(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("head")) {
                    rightside.setBodyPart("head", Assets.faces[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("shoulder")) {
                    rightside.setBodyPart("shoulder", Assets.shoulders[Integer.parseInt(str)], Integer.parseInt(str));
                }
                System.out.println(currentNode);
                if (currentNode.equalsIgnoreCase("legs")) {
                    rightside.setBodyPart("leg", Assets.pants[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("torso")) {
                    rightside.setBodyPart("torso", Assets.shirts[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("elbow")) {
                    rightside.setBodyPart("elbow", Assets.arms[Integer.parseInt(str)], Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("weapon")) {
                    b = new Weapon();
                }
                if (currentNode.equalsIgnoreCase("spritenumber")) {
                    int spritenum = Integer.parseInt(str);
                    if(b == null)
                        b = new Weapon();
                    if (spritenum != -1) {
                        shouldequip = true;
                        b.setSprite(Assets.weapons_sprites.get(spritenum));
                    } else {
                        shouldequip = false;
                    }
                }
                if (currentNode.equalsIgnoreCase("mindamage")) {
                    b.setMin_damage(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("maxdamage")) {
                    b.setMax_damage(Integer.parseInt(str));
                }
                if (currentNode.equalsIgnoreCase("hearts")) {
                    b.setHp_multiplier(Integer.parseInt(str));
                    if (shouldequip)
                        rightside.setEquipped_weapon(b);
                }
                break;
            case UNCLOCKS:
                System.out.println("UNCLOCKS: " + currentNode);
                if(currentNode.equals(""))
                    break;
                if(currentNode.equalsIgnoreCase("gearUnlocks")) {


                    String[] splits = str.split(";");
                    int r = Integer.parseInt(splits[0].toString());
                    int c = Integer.parseInt(splits[1].toString());

                    unclocks = new int[r][c];
                    int index = 0;
                    for (int i = 0; i < r; i++) {
                        for (int j = 0; j < c; j++) {
                            unclocks[i][j] = splits[2].charAt(i * c + j) - 48;
                        }
                    }
                } else if (currentNode.equalsIgnoreCase("WeaponUnlocks")){
                    String[] splits = str.split(";");
                    int r = Integer.parseInt(splits[0].toString());
                    System.out.println("r" + r);
                    weapon_unlocks = new int[r];
                    for (int i = 0; i < r; i++) {
                         weapon_unlocks[i] = splits[1].charAt(i) - 48;
                    }

                }else if(currentNode.equalsIgnoreCase("Unclocks")) {

                }else{
                    state = States.OTHER;
                }
                break;
        }
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        System.out.println("BGN " + localName);
        currentNode = localName;
        if (currentNode.equalsIgnoreCase("stats")) {
            state = States.STATS;
            stats = new int[7];
            stats[0] = -1;
            stats[1] = -1;
            stats[2] = 0;
            stats[3] = 0;
            stats[4] = 0;
            stats[5] = 0;
            stats[6] = 0;
        }
        if (currentNode.equalsIgnoreCase("character")) {
            charind++;
            if (charind == 1) {
                state = States.CHAR1;

            } else if (charind == 2) {
                state = States.CHAR2;
            }

        }
        if (currentNode.equalsIgnoreCase("unclocks")) {
            state = States.UNCLOCKS;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        System.out.println("END " + localName);
        currentNode = "";
        super.endElement(uri, localName, qName);
    }


    public static Character getLeftside() {
        return leftside;
    }

    public static Character getRightside() {
        return rightside;
    }
}
