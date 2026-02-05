# Troubleshooting Guide

Having issues with OGUI Enhanced? This guide will help you diagnose and fix common problems.

---

## 🔍 Diagnostic Checklist

Before troubleshooting specific issues, run through this checklist:

- [ ] Server version 1.16.5+ (check with `/version`)
- [ ] OGUI plugin loaded (check `/plugins`)
- [ ] Configuration valid YAML (no tabs, proper indentation)
- [ ] Ran `/ogui reload` after config changes
- [ ] Checked console for error messages
- [ ] Tested with minimum conditions first
- [ ] Required plugins installed for conditions used

---

## ⚠️ Plugin Won't Load

### Symptoms
- Plugin shows red in `/plugins`
- Errors on server startup
- Commands don't work

### Solutions

**Check Java Version:**
```bash
java -version
```
- Need Java 8 or higher
- Java 17+ recommended

**Verify JAR File:**
1. Re-download OGUI-Enhanced.jar
2. Check file isn't corrupted
3. Verify file size (should be ~500KB-1MB)

**Check Dependencies:**
```
/plugins
```
- Vault (if using economy)
- Other optional plugins

**Console Errors:**
Look for:
```
[OGUI] Could not load plugin
[OGUI] Incompatible server version
[OGUI] Missing dependency
```

**Fix:**
1. Delete OGUI jar
2. Re-download latest version
3. Restart server
4. Check console logs

---

## 🚫 Commands Not Working

### `/ogui` says "Unknown command"

**Cause:** Plugin not loaded

**Solutions:**
1. Check `/plugins` - OGUI should be green
2. Look for load errors in console
3. Reinstall plugin

### Custom shop commands don't work (e.g., `/shop`)

**Cause:** Not registered or not reloaded

**Solutions:**

**Check Configuration:**
```yaml
guis:
  my_shop:
    commands:
      - "shop"  # ✓ Correct
    # commands:   # ✗ Wrong indentation
    #   - "shop"
```

**Reload:**
```
/ogui reload
```

**Verify Registration:**
Check console for:
```
[OGUI] Registered command: /shop -> GUI: my_shop
```

**Common Mistakes:**
```yaml
# ✗ Wrong - missing commands field
guis:
  my_shop:
    title: "Shop"
    # No commands!

# ✓ Correct
guis:
  my_shop:
    title: "Shop"
    commands:
      - "shop"
```

### `/ogui reload` says "No permission"

**Cause:** Missing permission

**Fix:**
```
/lp user <yourname> permission set ogui.reload
```

---

## 🛒 Shop Won't Open

### GUI Opens But Is Empty

**Cause:** No items configured or wrong slots

**Check:**
```yaml
guis:
  my_shop:
    items:
      "13":  # ✓ Valid slot (0-53)
        material: DIAMOND
      
      "100":  # ✗ Invalid slot (too high)
        material: EMERALD
```

**Valid Slots:**
- Rows 1-6
- Slots 0-53
- Format: "10", "13", "22" (strings with quotes)

**Fix:**
1. Ensure items section exists
2. Use valid slot numbers
3. Check indentation

### GUI Opens But Items Don't Display

**Cause:** Invalid material names

**Check Console:**
```
[OGUI] Invalid material: DAIMOND  # Typo!
```

**Valid Materials:**
Check [Spigot JavaDocs](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html)

**Common Typos:**
```yaml
# ✗ Wrong
material: DAIMOND
material: EMERLD
material: NETHERITE_INGOT  # Typo

# ✓ Correct
material: DIAMOND
material: EMERALD
material: NETHERITE_INGOT
```

### Custom Items Don't Display

**ItemsAdder Items:**

**Check:**
1. ItemsAdder installed: `/plugins`
2. Item exists: `/iainfo <item_id>`
3. Correct syntax:

```yaml
items:
  "10":
    material: ruby        # Fallback
    item_type: itemsadder  # Required
    item_id: ruby         # Required
```

**Nexo Items:**

**Check:**
1. Nexo installed: `/plugins`
2. Item exists in Nexo config
3. Correct syntax:

```yaml
items:
  "10":
    material: sapphire
    item_type: nexo
    item_id: sapphire
```

---

## 💰 Conditions Not Working

### Players Can Buy Without Requirements

**Cause:** Conditions not configured or wrong format

**Check:**
```yaml
# ✗ Wrong - missing conditions field
items:
  "10":
    material: DIAMOND
    commands:
      - "give {player} diamond 1"

# ✓ Correct
items:
  "10":
    material: DIAMOND
    commands:
      - "give {player} diamond 1"
    conditions:
      - type: VAULT_MONEY
        amount: 500
```

**Common Mistakes:**

**Wrong Indentation:**
```yaml
# ✗ Wrong
items:
  "10":
    material: DIAMOND
conditions:  # Too far left!
  - type: VAULT_MONEY

# ✓ Correct
items:
  "10":
    material: DIAMOND
    conditions:  # Indented under item
      - type: VAULT_MONEY
```

**Missing Type:**
```yaml
# ✗ Wrong
conditions:
  - currency: gems
    amount: 1000

# ✓ Correct
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
```

### VAULT_MONEY Not Working

**Check:**
1. Vault installed: `/plugins`
2. Economy plugin installed (EssentialsX, etc.)
3. Vault registered: `/vault-info`

**Test:**
```
/eco give <yourname> 1000
/balance
```

**Configuration:**
```yaml
conditions:
  - type: VAULT_MONEY
    amount: 500  # Not "500.0" or "$500"
```

### OREO_CURRENCY Not Working

**Check:**
1. OreoEssentials installed: `/plugins`
2. Currency exists:
   ```
   /currency list
   ```
3. Player has currency:
   ```
   /currency balance <yourname> gems
   ```

**Common Issues:**

**Wrong Currency Name:**
```yaml
# ✗ Wrong (case matters!)
conditions:
  - type: OREO_CURRENCY
    currency: Gems  # Capital G

# ✓ Correct
conditions:
  - type: OREO_CURRENCY
    currency: gems  # Lowercase
```

**Currency Doesn't Exist:**
```
[OGUI] Currency not found: gem
[OGUI] Did you mean: gems?
```

Fix: Create currency in OreoEssentials config

### ITEM Condition Not Working

**Check:**
1. Material name correct
2. Player has items in inventory
3. Amount is reachable

**Debug:**
```yaml
conditions:
  - type: ITEM
    material: DIAMOND  # Check spelling
    amount: 10         # Check player has 10+
```

**Give Test Items:**
```
/give <yourname> diamond 10
```

### PERMISSION Not Working

**Check:**
1. Permission plugin installed
2. Permission granted:
   ```
   /lp user <name> permission check rank.vip
   ```

**Granting Permissions:**
```
/lp user <name> permission set rank.vip
```

### WORLDGUARD_REGION Not Working

**Check:**
1. WorldGuard installed
2. WorldEdit installed (dependency)
3. Region exists:
   ```
   /rg info <region_name>
   ```
4. Player in region (test with F3)

**Common Issues:**

**Wrong Region Name:**
```yaml
# ✗ Wrong
conditions:
  - type: WORLDGUARD_REGION
    region: spawn-area  # Typo

# ✓ Correct
conditions:
  - type: WORLDGUARD_REGION
    region: spawn  # Exact name
```

**Member Requirement:**
```yaml
# Just be in region
conditions:
  - type: WORLDGUARD_REGION
    region: spawn
    require_member: false

# Must be member
conditions:
  - type: WORLDGUARD_REGION
    region: vip-zone
    require_member: true
```

### PLACEHOLDER Not Working

**Check:**
1. PlaceholderAPI installed
2. Expansion installed:
   ```
   /papi list
   /papi ecloud download Vault
   ```
3. Placeholder valid:
   ```
   /papi parse <yourname> %vault_eco_balance%
   ```

**Common Issues:**

**Invalid Placeholder:**
```yaml
# ✗ Wrong (typo)
placeholder: "%vault_balance%"

# ✓ Correct
placeholder: "%vault_eco_balance%"
```

**Wrong Operator:**
```yaml
# ✗ Wrong
operator: "greater than"

# ✓ Correct
operator: ">="
# or
operator: "greater_than_equal"
```

**Value Type Mismatch:**
```yaml
# ✗ Wrong (comparing number as string)
placeholder: "%vault_eco_balance%"
operator: "equals"
value: "1000"  # Won't work for numbers

# ✓ Correct (use comparison)
placeholder: "%vault_eco_balance%"
operator: ">="
value: "1000"
```

---

## 📝 Configuration Errors

### YAML Parse Error

**Symptoms:**
```
[OGUI] Failed to load guis.yml
[OGUI] YAML parse error at line 15
```

**Common Causes:**

**Tab Characters:**
```yaml
# ✗ Wrong (tab used)
items:
	"10":  # <- That's a tab!

# ✓ Correct (spaces only)
items:
  "10":  # <- Spaces
```

**Fix:** Replace all tabs with spaces

**Mismatched Quotes:**
```yaml
# ✗ Wrong
name: "&6Gold Text  # Missing closing quote

# ✓ Correct
name: "&6Gold Text"
```

**Wrong Indentation:**
```yaml
# ✗ Wrong
guis:
  my_shop:
    title: "Shop"
  items:  # Should be indented!

# ✓ Correct
guis:
  my_shop:
    title: "Shop"
    items:
```

**Validation:**
Use online YAML validator:
- https://www.yamllint.com/
- https://codebeautify.org/yaml-validator

### Color Codes Not Working

**Check Format:**
```yaml
# ✗ Wrong
name: "§6Gold Text"  # Using § symbol

# ✓ Correct
name: "&6Gold Text"  # Using & symbol
```

### Special Characters Breaking Config

**Escape Special Characters:**
```yaml
# ✗ Wrong
name: "Item: "Cost: $500""  # Quotes inside quotes

# ✓ Correct
name: 'Item: "Cost: $500"'  # Use single quotes
# or
name: "Item: \"Cost: $500\""  # Escape quotes
```

---

## 🔧 Commands Not Executing

### Commands Don't Run When Item Clicked

**Check Console:**
Look for:
```
[OGUI] Executing: give PlayerName diamond 1
```

**Common Issues:**

**Placeholder Not Replaced:**
```yaml
# ✗ Wrong
commands:
  - "give player diamond 1"  # Literal "player"

# ✓ Correct
commands:
  - "give {player} diamond 1"  # {player} placeholder
```

**Invalid Command:**
```yaml
# ✗ Wrong
commands:
  - "/give {player} diamond 1"  # Don't include /

# ✓ Correct
commands:
  - "give {player} diamond 1"
```

**Command Needs Console:**
All commands run as console by default. If command needs player context, it may fail.

**Workaround:**
Use player-compatible commands or Skript/CommandPrompter.

### Multiple Commands Only First Runs

**Check:**
```yaml
# ✗ Wrong (missing dash)
commands:
  - "give {player} diamond 1"
  "tellraw {player} {\"text\":\"Success\"}"

# ✓ Correct
commands:
  - "give {player} diamond 1"
  - "tellraw {player} {\"text\":\"Success\"}"
```

---

## 🐌 Performance Issues

### Shop Laggy When Opening

**Causes:**
1. Too many conditions
2. Expensive condition checks
3. Large item counts
4. Too many players

**Solutions:**

**Optimize Conditions:**
```yaml
# ✗ Slow (redundant checks)
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
  - type: PLACEHOLDER
    placeholder: "%oreo_gems%"
    operator: ">="
    value: "1000"  # Duplicate check!

# ✓ Fast (single check)
conditions:
  - type: OREO_CURRENCY
    currency: gems
    amount: 1000
```

**Reduce GUI Size:**
```yaml
# Use smaller GUIs when possible
rows: 3  # Instead of rows: 6
```

**Limit Condition Count:**
- Use 1-5 conditions per item
- Avoid 10+ conditions

### Server TPS Dropping

**Check:**
```
/tps
```

**OGUI Unlikely Cause:**
OGUI is lightweight. Check other plugins first.

**Debug:**
1. Remove OGUI
2. Check if TPS improves
3. If yes, contact support
4. If no, issue is elsewhere

---

## 📊 Debugging Steps

### Enable Verbose Logging

Add to server startup:
```
-Dogui.debug=true
```

### Check Console Logs

Look for:
```
[OGUI] Loading GUI: my_shop
[OGUI] Registered command: /shop
[OGUI] Player PlayerName clicked item at slot 13
[OGUI] Checking condition: OREO_CURRENCY
[OGUI] Condition passed
[OGUI] Executing command: give PlayerName diamond 1
```

### Test Incrementally

1. Start with empty GUI
2. Add one item
3. Test
4. Add condition
5. Test
6. Add more conditions
7. Test
8. Repeat

### Isolate Issues

**Create Test Shop:**
```yaml
guis:
  test_shop:
    title: "&aTest Shop"
    rows: 1
    commands:
      - "testshop"
    items:
      "4":
        material: DIAMOND
        name: "&bTest Item"
        commands:
          - "give {player} diamond 1"
        # No conditions
```

If this works, add conditions one by one.

---

## 🆘 Getting Help

### Before Asking for Help

Collect this information:

1. **Server Details:**
   - Minecraft version (`/version`)
   - Server software (Spigot/Paper/etc.)
   - Java version

2. **Plugin Versions:**
   - OGUI version
   - Vault version (if using)
   - OreoEssentials version (if using)
   - Other relevant plugins

3. **Error Information:**
   - Console errors (full log)
   - When error occurs
   - Steps to reproduce

4. **Configuration:**
   - Your guis.yml
   - Relevant sections only

### Where to Get Help

**Documentation:**
- [FAQ](FAQ.md)
- [Installation Guide](Installation-Guide.md)
- [Condition System](Condition-System.md)

**Support Channels:**
- Discord Server
- GitHub Issues
- Email Support

### Creating Good Bug Reports

Include:
1. Clear title
2. Server/plugin versions
3. Steps to reproduce
4. Expected behavior
5. Actual behavior
6. Console errors
7. Relevant config sections
8. Screenshots (if applicable)

**Example:**
```
Title: OREO_CURRENCY condition not working

Server: Paper 1.19.4
OGUI: v2.0.0
OreoEssentials: v1.0.0

Steps:
1. Configure gems shop
2. Player has 1000 gems
3. Item requires 500 gems
4. Player clicks item
5. Nothing happens

Expected: Item purchased, gems deducted
Actual: No response, no errors

Console: (no errors)

Config:
[paste relevant section]
```

---

## ✅ Prevention Tips

### Before Going Live

- [ ] Test on staging server
- [ ] Verify all conditions
- [ ] Test with different player roles
- [ ] Check console for warnings
- [ ] Backup configurations
- [ ] Document your setup
- [ ] Create test accounts
- [ ] Monitor first few transactions

### Best Practices

1. **Start Simple:**
   - Begin with basic conditions
   - Add complexity gradually
   - Test after each change

2. **Keep Backups:**
   ```bash
   cp guis.yml guis.yml.backup
   ```

3. **Use Version Control:**
   - Git repository
   - Track changes
   - Easy rollbacks

4. **Document Changes:**
   - Comment your configs
   - Keep changelog
   - Note special cases

5. **Regular Testing:**
   - Test after updates
   - Test new features
   - Test edge cases

---

## 📞 Contact Support

Still stuck?

**Community Support:**
- Discord: [link]
- Forum: [link]

**Professional Support:**
- Email: support@example.com
- Priority support for premium users

**Emergency Issues:**
1. Check [Status Page](https://status.example.com)
2. Review recent updates
3. Contact support with logs

---

Remember: 99% of issues are configuration errors. Take your time, double-check syntax, and test incrementally!

**Good luck!** 🚀
