# Contributing to Stocks App 📈

First off, thank you for considering contributing to the **Stocks App**! Your involvement helps make this tool better for everyone.

To maintain a high-quality codebase and a smooth workflow, please follow these guidelines.

---

## 🏗 Codebase Architecture

The project follows the **MVVM (Model-View-ViewModel)** architectural pattern combined with a Repository pattern to ensure a clean separation of concerns and testability.

### Package Mapping
*   **`adapters/`**: RecyclerView adapters for lists like Top Gainers, Losers, and Watchlists.
*   **`api/`**: Retrofit interfaces (`StocksAPI.kt`) and network client configurations (`RetrofitInstance.kt`).
*   **`db/`**: Room database logic, including the Database class, DAOs, and entities.
*   **`models/`**: POJOs and Data classes for API responses and local persistence.
*   **`repository/`**: The single source of truth managing data flow between the API and local storage.
*   **`ui/`**: 
    *   **Fragments**: UI screens (Home, Details, Watchlist, etc.) in `ui/fragments/`.
    *   **ViewModels**: Business logic and UI state management.
*   **`util/`**: Utility classes, extension functions, and global constants.

---

## 🔍 Where to Find Things

*   **UI changes?** Check `ui/fragments/` and the corresponding XML files in `res/layout/`.
*   **New API endpoint?** Modify `api/StocksAPI.kt` and add response models to the `models/` folder.
*   **Database updates?** Edit the relevant DAO or Entity in the `db/` or `models/` packages.
*   **Business logic?** Check the `repository/` or the specific `ViewModel` in the `ui/` package.

---

## 🛠 How to Contribute

### 1. Find an Issue
Browse the [Issues](https://github.com/SDE-Solutions/StocksApp/issues) tab. If you find something you'd like to work on:

> **Important:** Before starting, please comment on the issue and ask for it to be assigned to you.

### 2. Standard Workflow
1.  **Fork** the repository to your own GitHub account.
2.  **Clone** your fork locally:
    ```bash
    git clone https://github.com/your-username/StocksApp.git
    ```
3.  **Create a New Branch** for the specific issue:
    ```bash
    git checkout -b issue-<issue-number>
    ```
4.  **Solve the Issue**:
    *   Implement your changes and ensure they follow the project's coding style.
    *   Test your changes thoroughly on an emulator or physical device.
5.  **Commit your changes** with a clear message:
    ```bash
    git commit -m "Fixes issue #<issue-number>: <description-of-fix>"
    ```
6.  **Push the Changes** to your fork:
    ```bash
    git push origin issue-<issue-number>
    ```

### 3. Create a Pull Request
1.  Go to the original repository and click on the **Pull requests** tab.
2.  Click **New pull request** and select your branch.
3.  Provide a detailed description of your changes and submit the PR.

---

## ✅ Contribution Checklist
- [ ] Code follows MVVM structure.
- [ ] Changes tested on an Android device/emulator.
- [ ] Branch name follows `issue-<number>` format.
- [ ] Commit messages reference the issue number.

We appreciate your contributions and look forward to working with you! 🚀
