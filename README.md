# computer-repair-shop-
here I am building a software for a computer repair shop as part of my BCA(Hons./Hons. with Research)2025-29 semester 3 internal component for CSIT120

1. Scope of the System
The scope defines what your application actually does. For a repair shop, the system moves operations from paper ledgers to a centralized database.
* Customer & Device Logging: Recording customer details alongside device specifications, serial numbers, and the reported issue.
* Ticketing & Status Tracking: Generating a unique ID for every repair job and tracking its lifecycle (e.g., Received -> Diagnosing -> Awaiting Parts -> Repaired -> Delivered).
* Inventory Management: Tracking spare parts (RAM, screens, hard drives) and alerting the manager when stock falls below a certain threshold.
* Technician Assignment: Allocating specific repair tickets to available technicians based on their workload.
* Billing & Invoicing: Calculating labor and parts costs, and generating a final printable receipt using AWT graphics capabilities.

2. The "Novelty" Factor
Since AWT is a foundational, older UI toolkit, the novelty of your project won't be flashy modern graphics. Instead, your presentation should position the novelty around efficiency, native performance, and architectural design.
* Ultra-Lightweight & Native: Unlike web apps (which require browsers) or heavy modern frameworks, AWT maps directly to native OS GUI components. This makes the software extremely lightweight, allowing it to run smoothly on the older, lower-end hardware typically found at a small repair shop's front desk.
* Offline-First Reliability: Small businesses often suffer from internet outages. Your system operates entirely offline on a local network, ensuring business continuity.
* Custom Data Flow: The real novelty lies under the hood. You can highlight how the system efficiently manages memory and processes data natively without relying on heavy external libraries.
