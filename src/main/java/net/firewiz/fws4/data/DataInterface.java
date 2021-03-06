package net.firewiz.fws4.data;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;

public class DataInterface {
	public static Material[] crafted;

	public static class StaticEntityData {
		public char healthBarColor;
		public boolean poorDrops;
		public boolean rareDrops;

		StaticEntityData(char hBC, boolean pd, boolean rd) {
			healthBarColor = hBC;
			poorDrops = pd;
			rareDrops = rd;
		}
	}

	public static HashMap<EntityType, StaticEntityData> esd;

	public static class ItemData {
		public String name;
		public boolean isTool;
		public boolean isArmor;
		public boolean isTree;

		ItemData(String n) {
			name = n;
			isTool = isArmor = isTree = false;
		}

		ItemData(String n, boolean iT, boolean iA) {
			this(n);
			isTool = iT;
			isArmor = iA;
		}

		ItemData(String n, boolean iT, boolean iA, boolean iR) {
			this(n, iT, iA);
			isTree = iR;
		}
	}

	static HashMap<Material, ItemData> id;

	public static ItemData getItemData(Material m) {
		if (m == null)
			throw new IllegalArgumentException();
		return (id.get(m) == null) ? new ItemData("Unknown Item") : id.get(m);
	}

	public static void init() {
		String[] s = Data.getConfig().getList("crafted").toArray(new String[0]);
		crafted = new Material[s.length];
		for (int i = 0; i < s.length; i++) {
			crafted[i] = Material.getMaterial(s[i]);
		}

		esd = new HashMap<EntityType, StaticEntityData>();

		ConfigurationSection entity = Data.getConfig().getConfigurationSection(
				"entity");
		for (String key : entity.getKeys(false)) {
			ConfigurationSection specific = entity.getConfigurationSection(key);
			StaticEntityData ed = new StaticEntityData(specific.getString(
					"color").charAt(0), specific.getBoolean("poorDrops"),
					specific.getBoolean("rareDrops"));
			esd.put(EntityType.valueOf(key), ed);
		}

		id = new HashMap<Material, ItemData>();
		ConfigurationSection item = Data.getConfig().getConfigurationSection(
				"item");
		for (String key : item.getKeys(false)) {
			ConfigurationSection specific = item.getConfigurationSection(key);
			ItemData id = new ItemData(specific.getString("name"),
					specific.getBoolean("tool", false), specific.getBoolean(
							"armor", false), specific.getBoolean("tree", false));
			DataInterface.id.put(Material.valueOf(key), id);
		}
	}

	public static StaticEntityData getEntityData(EntityType e) {
		if (esd.get(e) != null)
			return esd.get(e);
		return esd.get(EntityType.UNKNOWN);
	}
}
