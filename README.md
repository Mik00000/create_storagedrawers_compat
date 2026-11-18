# Create & Storage Drawers Compat

Interoperability and bugfix bridge between **Create** and **Storage Drawers** on Minecraft 1.21.1 (NeoForge).

---

## 🔧 What It Fixes

1. **Packager Unpack Simulation:**
   - Standard Create Packagers simulate item insertion into storage blocks. Storage Drawers' default item handler often reported incorrect simulated capacities or failed unpack transactions.
   - Adds a dedicated `StorageDrawersUnpackingHandler` to ensure seamless packaging and unpacking into drawers.

2. **Stockpile Switch / Threshold Switch Accuracy:**
   - Fixes the 128-item cap when attaching Create Threshold Switches to large Storage Drawers and drawer controllers.
   - Properly reads full drawer capacity and stack counts so comparator/threshold redstone signals accurately represent percentage fullness.

---

## 📦 Compatibility

- **Minecraft:** `1.21.1`
- **NeoForge:** `21.1.x`
- **Create:** `6.0.x`+
- **Storage Drawers:** `13.0.x`+

---

## 📄 License

This mod is licensed under the **MIT License**.
