# Installation Guide

Welcome to OGUI Enhanced! This guide will help you install and configure the plugin on your server.

---

## 📋 Requirements

### Server Requirements
- **Minecraft Version:** 1.16.5 or higher
- **Server Software:** Spigot, Paper, or Purpur
- **Java Version:** Java 8 or higher (Java 17+ recommended)

### Required Dependencies
- **Vault** - For economy integration (optional but recommended)

### Optional Dependencies
- **OreoEssentials** - For custom currency support
- **ItemsAdder** - For custom items
- **Nexo** - For custom items
- **WorldGuard** - For region-based conditions
- **PlaceholderAPI** - For dynamic conditions
- **LuckPerms** (or any permission plugin) - For permission conditions

---

## 📦 Step 1: Download

### Get OGUI Enhanced
1. Download the latest `OGUI-Enhanced.jar` from:
   - GitHub Releases
   - SpigotMC
   - Your purchase platform

2. Verify the file:
   - File size should be approximately 500KB-1MB
   - Filename: `OGUI-Enhanced-vX.X.X.jar`

### Get Dependencies
Download required and optional dependencies:

**Vault** (Required)
```
https://www.spigotmc.org/resources/vault.34315/
```

**OreoEssentials** (Optional - for custom currencies)
```
https://www.spigotmc.org/resources/oreoessentials.111940/
```

**ItemsAdder** (Optional - for custom items)
```
https://www.spigotmc.org/resources/itemsadder.73355/
```

---

## 🚀 Step 2: Installation

### Basic Installation

1. **Stop your server**
   ```bash
   # In server console
   stop
   ```

2. **Place JAR file**
   ```bash
   # Copy OGUI-Enhanced.jar to plugins folder
   cp OGUI-Enhanced.jar /path/to/server/plugins/
   ```

3. **Install dependencies**
   ```bash
   # Copy Vault.jar and other dependencies
   cp Vault.jar /path/to/server/plugins/
   cp OreoEssentials.jar /path/to/server/plugins/  # If using
   ```

4. **Start your server**
   ```bash
   # Start the server
   ./start.sh  # or your startup script
   ```

### Verify Installation

Check the console for successful loading:
```
[OGUI] Enabling OGUI-Enhanced v2.0
[OGUI] ItemProvider initialized with support for: Vanilla, ItemsAdder, Nexo
[OGUI] Loaded 0 GUI definition(s).
[OGUI] OGUI Enhanced enabled with support for:
[OGUI] - OreoEssentials Custom Currencies
[OGUI] - ItemsAdder Custom Items
[OGUI] - Nexo Custom Items
[OGUI] - WorldGuard Regions
[OGUI] - PlaceholderAPI Conditions
[OGUI] - Multiple Condition Types (XP, Items, Permissions, etc.)
```

---

## ⚙️ Step 3: Configuration

### Initial Setup

1. **Locate config folder**
   ```
   plugins/OGUI/
   ├── guis.yml          # Main configuration file
   └── config.yml        # Plugin settings (future)
   ```

2. **Edit guis.yml**
   
   The plugin creates a default `guis.yml` on first run. You can:
   - Use the default configuration
   - Copy from our examples
   - Create your own from scratch

### Quick Configuration

Replace the default `guis.yml` with a simple shop:

```yaml
guis:
  test_shop:
    title: "&6Test Shop"
    rows: 3
    commands:
      - "shop"
    items:
      "13":
        material: DIAMOND
        name: "&bTest Item"
        lore:
          - "&7Click to receive a diamond"
        commands:
          - "give {player} diamond 1"
```

3. **Reload configuration**
   ```
   /ogui reload
   ```

4. **Test the shop**
   ```
   /shop
   ```

---

## 🔧 Step 4: Plugin Integration

### Vault Setup

**Install Vault:**
1. Download Vault from SpigotMC
2. Install an economy plugin (e.g., EssentialsX, CMI)
3. Restart server
4. Verify with: `/vault-info`

**Configure in OGUI:**
```yaml
items:
  "10":
    material: DIAMOND_SWORD
    name: "&bDiamond Sword"
    commands:
      - "give {player} diamond_sword 1"
    conditions:
      - type: VAULT_MONEY
        amount: 500
```

### OreoEssentials Setup

**Install OreoEssentials:**
1. Download OreoEssentials
2. Create currencies in OreoEssentials config
3. Restart server

**Configure in OGUI:**
```yaml
items:
  "10":
    material: DIAMOND
    name: "&bDiamond"
    commands:
      - "give {player} diamond 1"
    conditions:
      - type: OREO_CURRENCY
        currency: gems
        amount: 100
```

### ItemsAdder Setup

**Install ItemsAdder:**
1. Download ItemsAdder
2. Configure your custom items
3. Run `/iazip` to generate resource pack
4. Restart server

**Configure in OGUI:**
```yaml
items:
  "10":
    material: ruby  # Fallback material
    item_type: itemsadder
    item_id: ruby
    name: "&cRuby"
    commands:
      - "ia give {player} ruby 1"
```

### Nexo Setup

**Install Nexo:**
1. Download Nexo
2. Configure your custom items
3. Restart server

**Configure in OGUI:**
```yaml
items:
  "10":
    material: sapphire
    item_type: nexo
    item_id: sapphire
    name: "&9Sapphire"
    commands:
      - "nexo give {player} sapphire 1"
```

### WorldGuard Setup

**Install WorldGuard:**
1. Download WorldGuard
2. Download WorldEdit (dependency)
3. Create regions
4. Restart server

**Configure in OGUI:**
```yaml
items:
  "10":
    material: GOLD_BLOCK
    name: "&6VIP Item"
    commands:
      - "give {player} gold_block 1"
    conditions:
      - type: WORLDGUARD_REGION
        region: vip-zone
        require_member: true
```

### PlaceholderAPI Setup

**Install PlaceholderAPI:**
1. Download PlaceholderAPI
2. Install expansions: `/papi ecloud download Vault`
3. Restart server

**Configure in OGUI:**
```yaml
items:
  "10":
    material: EMERALD
    name: "&aRich Player Bonus"
    commands:
      - "give {player} emerald 64"
    conditions:
      - type: PLACEHOLDER
        placeholder: "%vault_eco_balance%"
        operator: ">="
        value: "10000"
```

---

## 🎯 Step 5: Testing

### Test Each Feature

**Test basic GUI:**
```
/ogui open test_shop
```

**Test Vault economy:**
1. Give yourself money: `/eco give [yourname] 1000`
2. Try to purchase an item requiring money

**Test OreoEssentials:**
1. Give yourself currency: `/currency add [yourname] gems 1000`
2. Try to purchase an item requiring gems

**Test permissions:**
1. Give yourself permission: `/lp user [yourname] permission set rank.vip`
2. Try to access VIP-only items

### Verify Logs

Check console for any errors:
```
[OGUI] Loaded 1 GUI definition(s).
[OGUI] Registered command: /shop -> GUI: test_shop
```

---

## 📝 Step 6: Production Setup

### Recommended Structure

Organize your GUIs logically:

```yaml
guis:
  # Main server shop
  main_shop:
    title: "&6Main Shop"
    commands: ["shop"]
    # ... items
  
  # VIP shop
  vip_shop:
    title: "&6VIP Shop"
    commands: ["vipshop"]
    # ... items
  
  # Donor shop
  donor_shop:
    title: "&6Donor Shop"
    commands: ["donorshop"]
    # ... items
```

### Performance Tips

1. **Limit GUI size** - Don't create 6-row GUIs unless necessary
2. **Optimize conditions** - Use the minimum conditions needed
3. **Cache lookups** - Avoid expensive operations in conditions
4. **Test before production** - Always test on a staging server first

### Security Considerations

1. **Validate commands** - Ensure command placeholders are safe
2. **Test permissions** - Verify permission checks work correctly
3. **Review prices** - Double-check economy values
4. **Backup configs** - Keep backups of working configurations

---

## 🔄 Updating

### Update Process

1. **Backup current installation**
   ```bash
   cp -r plugins/OGUI plugins/OGUI.backup
   ```

2. **Stop server**
   ```
   stop
   ```

3. **Replace JAR file**
   ```bash
   rm plugins/OGUI-Enhanced.jar
   cp OGUI-Enhanced-NEW-VERSION.jar plugins/
   ```

4. **Start server**
   ```bash
   ./start.sh
   ```

5. **Check for changes**
   - Review changelog
   - Test configurations
   - Update configs if needed

### Configuration Migration

If the new version requires config changes:

1. **Read migration guide** - Check for breaking changes
2. **Update guis.yml** - Add new required fields
3. **Test thoroughly** - Verify all features work
4. **Monitor logs** - Watch for deprecation warnings

---

## 🆘 Troubleshooting Installation

### Plugin Won't Load

**Error:** `Could not load 'OGUI-Enhanced.jar'`

**Solutions:**
1. Check Java version: `java -version`
2. Verify JAR isn't corrupted - re-download
3. Check file permissions: `chmod 644 OGUI-Enhanced.jar`

### Dependencies Missing

**Error:** `Could not find Vault`

**Solutions:**
1. Install Vault from SpigotMC
2. Check Vault is loaded: `/plugins`
3. Verify plugin.yml has correct softdepend

### Configuration Errors

**Error:** `No guis section found in guis.yml`

**Solutions:**
1. Verify YAML syntax with online validator
2. Check for tab characters (use spaces only)
3. Restore from backup or use example config

### Condition Not Working

**Error:** `Condition type 'OREO_CURRENCY' not working`

**Solutions:**
1. Check OreoEssentials is installed: `/plugins`
2. Verify currency exists in OreoEssentials
3. Check currency name matches exactly (case-sensitive)
4. Review console logs for specific errors

---

## ✅ Post-Installation Checklist

- [ ] Server version compatible (1.16.5+)
- [ ] OGUI Enhanced JAR in plugins folder
- [ ] Vault installed (if using economy)
- [ ] Optional plugins installed (OreoEssentials, etc.)
- [ ] guis.yml configured
- [ ] Plugin loads without errors
- [ ] Test shop command works
- [ ] Conditions check correctly
- [ ] Commands execute properly
- [ ] No console errors
- [ ] Players can use shops
- [ ] Backup created

---

## 📚 Next Steps

Now that OGUI Enhanced is installed:

1. **Learn the basics** - Read [Quick Start Tutorial](Quick-Start-Tutorial.md)
2. **Explore features** - Check [Condition System](Condition-System.md)
3. **See examples** - Browse [Shop Examples](Shop-Examples.md)
4. **Get help** - Join our [Discord](https://discord.gg/yourserver)

---

## 🎉 Congratulations!

OGUI Enhanced is now installed and ready to use! 

If you encounter any issues, check our [Troubleshooting Guide](Troubleshooting.md) or ask for help in our support channels.

**Happy configuring!** 🚀
