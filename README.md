# 🖥️ Computer Repair Shop Management System

A standalone desktop application built with **Java AWT** for managing the day-to-day operations of a computer repair shop: customers, their devices, repair jobs, and billing, all in one window with data that persists between sessions.

> Developed as the individual Java AWT project for **CSIT120 – Object Oriented Programming Using Java**, Amity University Kolkata (Amity Institute of Information Technology).

---

## 📌 Problem Statement

Small repair shops often track customers, devices, repair progress, and payments in paper registers or scattered notes. This makes it slow to look up a device's status, easy to lose records, and error-prone to compute bills. This application replaces that manual process with a simple, offline desktop tool that keeps everything linked together (customer → device → repair job → bill) and searchable.

## ✨ Features

- **Customer Management**: add, update, delete, and browse customers (name, contact, email, address).
- **Device Management**: register devices (Desktop, Laptop, Printer, Monitor, Other) against a customer, with brand, model, serial number, and reported problem.
- **Repair Job Tracking**: create jobs for a device and track diagnosis, status, technician, service description, parts used, and service charge.
- **Billing**: generate or update a bill per job with automatic total calculation (service charge + additional charge) and payment status.
- **Search**: look up customers (by name or ID), devices (by ID or serial number), and repair jobs (by ID or status).
- **Persistent storage**: all records are saved automatically to a local file using Java object serialization.
- **Input validation & feedback**: required-field checks, numeric validation, and modal dialogs for messages and exit confirmation.
- **Menu bar navigation**: `File` (Save Now, Exit) and `Navigate` (jump to any module) in addition to the dashboard buttons.

### Job statuses
`Received` → `Diagnosis` → `Under Repair` → `Awaiting Parts` → `Ready for Collection` → `Completed` / `Cancelled`

### Payment statuses
`Unpaid` · `Partially Paid` · `Paid`

---

## 🧰 Tech Stack

| Item | Details |
|------|---------|
| Language | Java (JDK 8 or newer) |
| GUI toolkit | AWT (`java.awt`, `java.awt.event`) |
| Persistence | Java Serialization (`ObjectOutputStream` / `ObjectInputStream`) |
| Database | None. Data is stored in a single local file |

### AWT components used
`Frame`, `Panel`, `Label`, `Button`, `TextField`, `TextArea`, `Choice`, `List`, `Dialog`, `MenuBar`, `Menu`, `MenuItem`

### Layout managers used
`BorderLayout`, `GridLayout`, `FlowLayout`, `CardLayout`

### Event handling
`ActionListener`, `ItemListener`, `TextListener`, and `WindowAdapter` (for window-closing confirmation), implemented with lambdas.

---

## 🚀 Getting Started

### Prerequisites
- JDK 8 or higher installed and available on your `PATH`

Check with:
```bash
java -version
javac -version
```

### Clone, compile, and run

```bash
git clone https://github.com/<your-username>/<your-repo-name>.git
cd <your-repo-name>

# compile all source files
javac *.java

# run the application
java App
```

If your sources are inside a folder such as `src/`, compile from there or use `javac -d out src/*.java` and then `java -cp out App`.

On first launch the app starts with empty records. A file named `repairshop_data.ser` is created in the working directory once you add data or exit.

---

## 🗂️ Project Structure

```
.
├── App.java                 # Entry point (main)
├── MainFrame.java           # Main window: menu bar, CardLayout navigation, dialogs
├── DashboardPanel.java      # Landing screen with module buttons
│
├── CustomerPanel.java       # Customer module UI
├── DevicePanel.java         # Device module UI
├── RepairJobPanel.java      # Repair job module UI
├── BillingPanel.java        # Billing module UI
├── SearchPanel.java         # Search module UI
│
├── Customer.java            # Model: customer
├── Device.java              # Model: device (belongs to a customer)
├── RepairJob.java           # Model: repair job (belongs to a device)
├── Billing.java             # Model: bill (belongs to a repair job)
│
├── RepairShopManager.java   # Business logic: CRUD, search, ID generation
├── AppData.java             # Serializable container for all records
├── FileHandler.java         # Save / load AppData to disk
└── Constants.java           # Shared dropdown values and data file name
```

## 🏗️ Class Design

| Class | Responsibility |
|-------|----------------|
| `App` | Launches the application by creating `MainFrame`. |
| `MainFrame` | Top-level `Frame`; owns the menu bar, switches screens via `CardLayout`, and shows message/confirm dialogs. |
| `DashboardPanel` | Home screen with buttons that navigate to each module. |
| `CustomerPanel` | Form and list for adding, editing, and deleting customers. |
| `DevicePanel` | Form and list for registering and managing devices. |
| `RepairJobPanel` | Form and list for creating and updating repair jobs. |
| `BillingPanel` | Generates or updates bills and computes totals live. |
| `SearchPanel` | Runs keyword/ID searches across customers, devices, and jobs. |
| `Customer`, `Device`, `RepairJob`, `Billing` | Serializable data models. |
| `RepairShopManager` | Holds data in memory; provides add/update/delete/search and ID generation; saves after every change. |
| `AppData` | Bundles all record lists into one serializable object. |
| `FileHandler` | Reads and writes the `AppData` bundle to a file. |
| `Constants` | Central place for statuses, device types, and the data file name. |

### Data relationships

```
Customer 1 ──── * Device 1 ──── * RepairJob 1 ──── 0..1 Billing
```

---

## 📖 How to Use

1. **Add a customer** in *Customer Management*.
2. **Register their device** in *Device Management* (pick the customer from the dropdown).
3. **Create a repair job** in *Repair Jobs* (pick the device, enter the date received, e.g. `26-09-2026`, and the service charge).
4. **Update the job** as work progresses: change status, diagnosis, technician, and parts used.
5. **Generate the bill** in *Billing*: choose the job, add any additional charge, set payment status, and click *Generate / Update Bill*.
6. **Search** anytime from *Search Records*.
7. Use **File → Save Now** to save manually; records are also saved after each add/update/delete and on exit.

To select a record for editing, click it in the list on the right; its details load into the form.

---

## ⚠️ Known Limitations

- Deleting a customer, device, or job does **not** cascade to related records (e.g. a deleted customer's devices remain).
- The *Update* action on a repair job does not change the linked device or date received.
- Dates are entered as free text and are not validated against a format.
- Job status search requires the exact status name (case-insensitive).
- Data is stored in Java's binary serialization format, so it is not human-readable and may not load if the model classes change.

## 🔮 Future Scope

- Cascade/guarded deletes to keep records consistent
- Date picker and stricter validation for dates, phone numbers, and email
- Printable invoices / receipts
- Filter and sort lists; a dashboard with job and revenue summaries
- Technician management and status-change history
- Export to CSV or PDF
- Migrate storage to a database (e.g. SQLite) for reliability and reporting

---

## 🎓 Academic Context

- **Course:** CSIT120 – Object Oriented Programming Using Java
- **Institution:** Amity University Kolkata, Amity Institute of Information Technology
- **Academic Year:** Odd Semester 2026–2027
- **Project type:** Individual Java AWT desktop application

## 👤 Author

**Your Name**
Enrolment No.: `XXXXXXXX` · Class/Section: `XXXX`
GitHub: [@your-username](https://github.com/your-username)

## 📄 License

This project was created for academic purposes. Add a license of your choice (e.g. MIT) if you wish to share it openly.
