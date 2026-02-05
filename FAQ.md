# Frequently Asked Questions (FAQ)

Common questions and answers about OGUI Enhanced.

---

## General Questions

### What is OGUI Enhanced?

OGUI Enhanced is a powerful configuration-driven GUI plugin for Minecraft servers. It allows you to create custom shop GUIs with advanced condition systems, supporting multiple economies, custom items, and complex requirements.

### Is it free?

Check the plugin's listing page for current pricing information.

### What Minecraft versions are supported?

OGUI Enhanced supports Minecraft 1.16.5 and higher. Tested on Spigot, Paper, and Purpur.

### Do I need coding knowledge?

No! OGUI Enhanced is configuration-based. You only need to edit YAML files. However, basic YAML syntax knowledge helps.

---

## Installation & Setup

### What dependencies do I need?

**Required:**
- None (plugin works standalone)

**Optional (for specific features):**
- Vault - For economy integration
- OreoEssentials - For custom currencies
- ItemsAdder - For custom items
- Nexo - For custom items
- WorldGuard - For region conditions
- PlaceholderAPI - For dynamic conditions

### How do I install OGUI Enhanced?

1. Download the JAR file
2. Place in `plugins/` folder
3. Restart server
4. Configure `plugins/OGUI/guis.yml`

See [Installation Guide](Installation-Guide.md) for details.

### Can I use OGUI without OreoEssentials?

Yes! OGUI works with:
- Just Vault economy
- Just permissions
- Just item requirements
- Any combination

OreoEssentials is only needed for custom currencies.

---

## Configuration

### Where is the configuration file?

`plugins/OGUI/guis.yml`

### How do I reload configuration?

Use the command:
```
/ogui reload
```

### Do I need to restart the server after changes?

No! Use `/ogui reload` to apply changes without restart.

### Can I have multiple shops?

Yes! Add multiple GUIs to `guis.yml`:

```yaml
guis:
  shop1:
    # ... config
  
  shop2:
    # ... config
  
  shop3:
    # ... config
```

### How many items can I have per GUI?

Up to 54 items (6 rows × 9 columns).

Slot numbers: 0-53

### Can I use color codes?

Yes! Use Minecraft color codes with `&`:

```yaml
name: "&6&lGold Bold Text"
lore:
  - "&aGreen text"
  - "&c&oRed italic"
```

---

## Conditions

### What condition types are available?

11 types:
1. VAULT_MONEY
2. OREO_CURRENCY
3. XP_LEVEL
4. XP_POINTS
5. ITEM
6. ITEM_CUSTOM_MODEL
7. ITEMSADDER
8. NEXO
9. PERMISSION
10. WORLDGUARD_REGION
11. PLACEHOLDER

See [Condition System](Condition-System.md) for details.

### Can I combine multiple conditions?

Yes! Add unlimited conditions. All must pass:

```yaml
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
  - type: XP_LEVEL
    amount: 50
  - type: PERMISSION
    permission: rank.vip
```

### Do I need all condition types?

No! Use only what you need. Missing plugins = condition type disabled.

### What happens if a player doesn't meet conditions?

- Error message shown
- GUI closes (or stays open based on config)
- No resources taken
- No commands executed

### Can I customize error messages?

Yes, for PlaceholderAPI conditions:

```yaml
conditions:
  - type: PLACEHOLDER
    placeholder: "%vault_eco_balance%"
    operator: ">="
    value: "10000"
    error_message: "&cCustom error here!"
```

Other conditions have automatic error messages.

---

## Economy & Currencies

### Can I use multiple currencies?

Yes! Combine any number:

```yaml
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
  - type: OREO_CURRENCY
    currency: tokens
    amount: 500
  - type: VAULT_MONEY
    amount: 5000
```

### How do I add OreoEssentials currencies?

1. Install OreoEssentials
2. Create currencies in OreoEssentials config
3. Use in OGUI:

```yaml
conditions:
  - type: OREO_CURRENCY
    currency: your_currency_name
    amount: 1000
```

### Does it work with EssentialsX economy?

Yes! Via Vault:

```yaml
conditions:
  - type: VAULT_MONEY
    amount: 500
```

### Can players earn currency from the GUI?

Yes! Use commands:

```yaml
commands:
  - "currency add {player} gems 100"
  - "eco give {player} 500"
```

---

## Custom Items

### What custom item plugins are supported?

- ItemsAdder
- Nexo
- Custom Model Data (resource packs)

### How do I use ItemsAdder items?

Display:
```yaml
items:
  "10":
    material: ruby
    item_type: itemsadder
    item_id: ruby
```

Require:
```yaml
conditions:
  - type: ITEMSADDER
    item_id: ruby
    amount: 5
```

### How do I use Nexo items?

Display:
```yaml
items:
  "10":
    material: sapphire
    item_type: nexo
    item_id: sapphire
```

Require:
```yaml
conditions:
  - type: NEXO
    item_id: sapphire
    amount: 3
```

### What if I don't have ItemsAdder/Nexo?

Items display as vanilla materials. Feature gracefully disabled.

---

## Commands

### What commands are available?

**Admin Commands:**
- `/ogui reload` - Reload configurations
- `/ogui open <id> [player]` - Open GUI for player

**Dynamic Commands:**
Commands from your config:
```yaml
guis:
  my_shop:
    commands:
      - "shop"      # Creates /shop
      - "store"     # Creates /store
```

### Can I change command names?

Yes! Edit the `commands:` list in config.

### How do I run commands as console?

All item commands run as console by default:

```yaml
commands:
  - "give {player} diamond 1"
  - "broadcast {player} bought an item!"
```

### Can I run commands as the player?

Not directly, but you can use Skript, CommandPrompter, or similar plugins to execute as player.

---

## Permissions

### What permissions does OGUI use?

**Plugin Commands:**
- `ogui.reload` - Reload configs
- `ogui.open` - Open GUIs for others
- `ogui.command.<gui_id>` - Use specific GUI command

**Dynamic Permissions:**
Created from your config:
```yaml
commands:
  - "shop"  # Creates permission: ogui.command.my_shop
```

### How do I make a VIP-only GUI?

Set permission on the command:

```yaml
guis:
  vip_shop:
    commands:
      - "vipshop"
```

Then: `/lp group vip permission set ogui.command.vip_shop`

Or use permission conditions:
```yaml
conditions:
  - type: PERMISSION
    permission: rank.vip
```

---

## Performance

### Will this lag my server?

No! OGUI Enhanced is optimized for performance:
- Lightweight condition checking
- Efficient memory usage
- Minimal CPU impact
- Async operations where possible

### How many shops can I have?

As many as you need! No hard limit. Tested with 50+ shops without issues.

### How many players can use shops simultaneously?

Thousands. GUI operations are per-player and non-blocking.

---

## Compatibility

### What plugins work with OGUI?

**Officially Supported:**
- Vault (any economy plugin)
- OreoEssentials
- ItemsAdder
- Nexo
- WorldGuard
- PlaceholderAPI
- LuckPerms (any permission plugin)

**Should Work:**
Most plugins that don't conflict with inventory handling.

### Does it work with chest shops?

Yes! OGUI is GUI-based and doesn't conflict with chest shop plugins.

### Can I use with other GUI plugins?

Yes! OGUI uses unique inventory IDs and doesn't conflict.

---

## Troubleshooting

### My shop won't open!

**Check:**
1. Did you run `/ogui reload`?
2. Is command registered in config?
3. Check console for errors
4. Verify YAML syntax

### Condition not working!

**Check:**
1. Is required plugin installed?
2. Is currency/item name correct? (case-sensitive)
3. Check console for errors
4. Test with simple conditions first

### Players can buy without meeting requirements!

**Check:**
1. Conditions are in `conditions:` section
2. Not using old `price`/`requirement` fields (unless intentional)
3. Correct indentation in YAML
4. Reload after changes

### "YAML parse error"

**Check:**
1. No tab characters (use spaces)
2. Proper indentation
3. Matching quotes
4. No special characters in names
5. Use YAML validator online

See [Troubleshooting Guide](Troubleshooting.md) for more.

---

## Advanced

### Can I create custom conditions?

Yes! OGUI has an API for developers:

```java
public class MyCondition implements Condition {
    // Your code
}
```

See [API Documentation](API-Documentation.md).

### Can I hook into OGUI events?

Yes! Events available:
- Pre-purchase
- Post-purchase
- GUI open/close

### Can I integrate with my custom plugin?

Yes! OGUI provides hooks and API.

### Is there a developer API?

Yes! See [API Documentation](API-Documentation.md).

---

## Migration

### I'm upgrading from old OGUI. Will my config work?

Yes! Old configs are fully compatible:

```yaml
# Old format (still works)
items:
  "10":
    material: DIAMOND_SWORD
    price: 500
    requirement: rank.vip
```

### How do I migrate to new format?

Replace old fields with conditions:

**Before:**
```yaml
price: 500
requirement: rank.vip
```

**After:**
```yaml
conditions:
  - type: VAULT_MONEY
    amount: 500
  - type: PERMISSION
    permission: rank.vip
```

See [Migration Guide](Migration-Guide.md).

---

## Support

### Where can I get help?

1. **Documentation** - This wiki
2. **Discord** - Community support
3. **GitHub Issues** - Bug reports
4. **Email** - Direct support

### How do I report a bug?

1. Check console for errors
2. Reproduce the issue
3. Post on GitHub Issues with:
   - OGUI version
   - Server version
   - Error logs
   - Configuration file
   - Steps to reproduce

### Can I request features?

Yes! Post feature requests on GitHub or Discord.

### Is there professional support?

Contact us for:
- Custom development
- Integration services
- Training
- Optimization

---

## Contributing

### Can I contribute to OGUI?

Yes! Contributions welcome:
- Bug fixes
- New features
- Documentation
- Translations

### How do I submit changes?

1. Fork on GitHub
2. Create feature branch
3. Make changes
4. Submit pull request

### Can I create addons?

Yes! OGUI has an API for addons.

---

## Licensing

### What license is OGUI under?

Check the plugin page for licensing information.

### Can I use this on multiple servers?

Check your license terms.

### Can I modify the code?

Check your license terms.

---

## Still Have Questions?

**Documentation:**
- [Home](Home.md)
- [Installation Guide](Installation-Guide.md)
- [Quick Start Tutorial](Quick-Start-Tutorial.md)
- [Condition System](Condition-System.md)

**Support Channels:**
- Discord Server
- GitHub Issues
- Email Support

**Can't find your answer?** Ask in Discord or create a GitHub issue!
