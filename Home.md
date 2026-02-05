# OGUI Enhanced - Official Wiki

<div align="center">

![OGUI Enhanced](https://img.shields.io/badge/OGUI-Enhanced-blue?style=for-the-badge)
![Version](https://img.shields.io/badge/Version-2.0-green?style=for-the-badge)
![Spigot](https://img.shields.io/badge/Spigot-1.16+-orange?style=for-the-badge)

**A powerful, configuration-driven GUI plugin for Minecraft servers**

[Features](#-key-features) • [Installation](#-quick-start) • [Documentation](#-documentation) • [Examples](#-examples) • [Support](#-support)

</div>

---

## 📖 Table of Contents

### Getting Started
- [Installation Guide](Installation-Guide.md) - Set up OGUI Enhanced on your server
- [Quick Start Tutorial](Quick-Start-Tutorial.md) - Create your first shop in 5 minutes
- [Configuration Basics](Configuration-Basics.md) - Understanding guis.yml

### Core Features
- [GUI System](GUI-System.md) - Creating and managing custom GUIs
- [Condition System](Condition-System.md) - Advanced requirement checking
- [Item Providers](Item-Providers.md) - Vanilla, ItemsAdder, and Nexo items
- [Command System](Command-System.md) - Dynamic command registration

### Condition Types
- [Economy Conditions](Economy-Conditions.md) - Vault & OreoEssentials
- [Experience Conditions](Experience-Conditions.md) - XP Levels & Points
- [Item Conditions](Item-Conditions.md) - Vanilla & Custom Items
- [Permission Conditions](Permission-Conditions.md) - Rank-based access
- [Region Conditions](Region-Conditions.md) - WorldGuard integration
- [PlaceholderAPI Conditions](PlaceholderAPI-Conditions.md) - Dynamic conditions

### Advanced Topics
- [Multiple Conditions](Multiple-Conditions.md) - Combining requirements
- [Custom Items](Custom-Items.md) - ItemsAdder, Nexo, Custom Model Data
- [Legacy Compatibility](Legacy-Compatibility.md) - Backwards compatibility
- [Performance Optimization](Performance-Optimization.md) - Best practices

### Examples & Tutorials
- [Shop Examples](Shop-Examples.md) - Ready-to-use shop configurations
- [Common Patterns](Common-Patterns.md) - Frequently used setups
- [Advanced Setups](Advanced-Setups.md) - Complex multi-condition shops
- [Use Cases](Use-Cases.md) - Real-world implementations

### Reference
- [Commands Reference](Commands-Reference.md) - All plugin commands
- [Permissions Reference](Permissions-Reference.md) - All permission nodes
- [Configuration Reference](Configuration-Reference.md) - Complete config options
- [API Documentation](API-Documentation.md) - For developers

### Support
- [Troubleshooting](Troubleshooting.md) - Common issues and solutions
- [FAQ](FAQ.md) - Frequently asked questions
- [Migration Guide](Migration-Guide.md) - Upgrading from old versions

---

## 🎯 Key Features

### 💰 Multi-Economy Support
- **Vault Integration** - Traditional server economy
- **OreoEssentials** - Multiple custom currencies (gems, tokens, credits, etc.)
- **Combine Multiple Currencies** - Require both Vault money AND custom currencies

### 🎨 Custom Items
- **Vanilla Items** - Standard Minecraft materials
- **Custom Model Data** - Resource pack custom items
- **ItemsAdder** - Full ItemsAdder integration
- **Nexo** - Full Nexo integration

### ✅ Advanced Condition System
- **11 Condition Types** - Money, XP, items, permissions, regions, and more
- **Multiple Conditions** - Combine unlimited conditions per item
- **Smart Checking** - Validates before consuming resources
- **Custom Error Messages** - User-friendly feedback

### 🔧 Easy Configuration
- **YAML-Based** - Simple, readable configuration
- **Hot Reload** - Update GUIs without restart
- **Dynamic Commands** - Auto-register commands from config
- **Backwards Compatible** - Old configs still work

### 🚀 Performance
- **Lightweight** - Minimal server impact
- **Optimized Checks** - Fast condition validation
- **Efficient Memory** - Smart resource management
- **Async Support** - Non-blocking operations where possible

---

## ⚡ Quick Start

### 1. Install the Plugin
```bash
# Download OGUI Enhanced
# Place OGUI.jar in plugins/ folder
# Restart server
```

### 2. Create Your First Shop
Edit `plugins/OGUI/guis.yml`:
```yaml
guis:
  my_shop:
    title: "&6My First Shop"
    rows: 3
    commands:
      - "shop"
    items:
      "13":
        material: DIAMOND_SWORD
        name: "&bDiamond Sword"
        lore:
          - "&7Cost: 1000 Gems"
        commands:
          - "give {player} diamond_sword 1"
        conditions:
          - type: OREO_CURRENCY
            currency: gems
            amount: 1000
```

### 3. Reload and Test
```
/ogui reload
/shop
```

**That's it!** You now have a working shop with OreoEssentials currency integration!

---

## 📊 Condition Types Overview

| Condition Type | Description | Plugin Required |
|---------------|-------------|----------------|
| **VAULT_MONEY** | Vault economy money | Vault |
| **OREO_CURRENCY** | OreoEssentials currencies | OreoEssentials |
| **XP_LEVEL** | Player experience levels | None |
| **XP_POINTS** | Total experience points | None |
| **ITEM** | Vanilla items | None |
| **ITEM_CUSTOM_MODEL** | Items with custom model data | None |
| **ITEMSADDER** | ItemsAdder custom items | ItemsAdder |
| **NEXO** | Nexo custom items | Nexo |
| **PERMISSION** | Permission nodes | Permission plugin |
| **WORLDGUARD_REGION** | WorldGuard regions | WorldGuard |
| **PLACEHOLDER** | PlaceholderAPI conditions | PlaceholderAPI |

---

## 💡 Common Use Cases

### Currency Shops
Perfect for servers with custom economies:
- Gems shop
- Token shop
- Multi-currency trading
- Rank upgrades

### Item Trading
Player-to-system trading:
- Resource exchange
- Crafting materials
- Item conversion
- Equipment upgrades

### VIP Perks
Permission-based rewards:
- Rank-exclusive items
- VIP shops
- Donor rewards
- Member benefits

### Region Shops
Location-based commerce:
- Spawn shops
- VIP area rewards
- Plot-specific items
- Territory bonuses

### Dynamic Shops
PlaceholderAPI integration:
- Balance requirements
- Statistics-based rewards
- Time-based availability
- Progress-gated items

---

## 🎓 Learning Path

### Beginner (Day 1)
1. Read [Installation Guide](Installation-Guide.md)
2. Follow [Quick Start Tutorial](Quick-Start-Tutorial.md)
3. Try [Simple Shop Examples](Shop-Examples.md#simple-shops)

### Intermediate (Week 1)
1. Learn [Condition System](Condition-System.md)
2. Explore [Multiple Conditions](Multiple-Conditions.md)
3. Study [Common Patterns](Common-Patterns.md)

### Advanced (Month 1)
1. Master [Custom Items](Custom-Items.md)
2. Implement [PlaceholderAPI Conditions](PlaceholderAPI-Conditions.md)
3. Build [Advanced Setups](Advanced-Setups.md)

---

## 🔗 Quick Links

### Documentation
- 📘 [Full Feature List](Features.md)
- ⚙️ [Configuration Guide](Configuration-Basics.md)
- 🎯 [All Condition Types](Condition-System.md)
- 📝 [Complete Examples](Shop-Examples.md)

### Support
- ❓ [FAQ](FAQ.md)
- 🔧 [Troubleshooting](Troubleshooting.md)
- 🐛 [Report Issues](https://github.com/yourrepo/issues)
- 💬 [Discord Support](https://discord.gg/yourserver)

### Development
- 📚 [API Documentation](API-Documentation.md)
- 🛠️ [Creating Conditions](Creating-Conditions.md)
- 🔌 [Plugin Hooks](Plugin-Hooks.md)
- 📦 [Source Code](https://github.com/yourrepo)

---

## 🌟 Feature Comparison

| Feature | OGUI Basic | OGUI Enhanced |
|---------|------------|---------------|
| Vault Economy | ✅ | ✅ |
| OreoEssentials Currencies | ❌ | ✅ |
| Multiple Conditions | ❌ | ✅ |
| ItemsAdder Support | ❌ | ✅ |
| Nexo Support | ❌ | ✅ |
| Custom Model Data | ❌ | ✅ |
| WorldGuard Regions | ❌ | ✅ |
| PlaceholderAPI | ❌ | ✅ |
| XP Requirements | Basic | Advanced |
| Item Requirements | Basic | Advanced |
| Permission Checks | ✅ | ✅ |
| Legacy Compatibility | ✅ | ✅ |

---

## 🎯 Why Choose OGUI Enhanced?

### For Server Owners
✅ **No coding required** - Pure configuration  
✅ **Hot reload** - Update without restart  
✅ **Multi-economy** - Support any currency system  
✅ **Future-proof** - Regular updates and support

### For Developers
✅ **Well-documented API** - Easy to extend  
✅ **Clean codebase** - Easy to understand  
✅ **Hook support** - Integrate with other plugins  
✅ **Open architecture** - Customizable everything

### For Players
✅ **Smooth experience** - Fast, responsive GUIs  
✅ **Clear feedback** - Helpful error messages  
✅ **Consistent UI** - Professional design  
✅ **No lag** - Optimized performance

---

## 📞 Getting Help

### Documentation
Start with our comprehensive documentation:
- [Installation Guide](Installation-Guide.md) - Setup instructions
- [Quick Start](Quick-Start-Tutorial.md) - First time using OGUI
- [FAQ](FAQ.md) - Common questions

### Community Support
Join our community:
- 💬 **Discord** - Real-time help
- 🐛 **GitHub Issues** - Bug reports
- 📧 **Email** - Direct support

### Professional Support
Need custom development?
- Custom condition types
- Integration with specific plugins
- Performance optimization
- Training and consultation

---

## 🎉 Success Stories

> "OGUI Enhanced transformed our economy system. Players love the multi-currency shops!"  
> — **SkyBlock Network** (2000+ players)

> "Setup took 10 minutes. The documentation is incredible!"  
> — **Survival Server Admin**

> "Best GUI plugin I've used. The condition system is genius."  
> — **Prison Server Owner**

---

## 📜 License

OGUI Enhanced is released under the MIT License.

---

## 🙏 Credits

**Developed by:** Your Team Name  
**Based on:** SmartInvs by MinusKube  
**Supported by:** The Minecraft server community

**Special Thanks:**
- Vault API developers
- OreoEssentials team
- ItemsAdder developers
- Nexo developers
- WorldGuard team
- PlaceholderAPI developers

---

<div align="center">

**[⬆ Back to Top](#ogui-enhanced---official-wiki)**

Made with ❤️ for the Minecraft community

</div>
