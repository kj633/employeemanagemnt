import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Employee {
    private static int nextId = 1000;
    private static int totalEmployees = 0;   // static member
    private int empId;
    private String name;
    private double salary;

    // CONSTRUCTOR OVERLOADING (compile-time polymorphism)
    public Employee(String name, double salary) {
        this.name = name;
        setSalary(salary);   // validation inside setter
        this.empId = nextId++;
        totalEmployees++;
    }

    public Employee(String name) {
        this(name, 0.0);
    }

    // ENCAPSULATION: getters and setters
    public int getEmpId() { return empId; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public void setName(String name) { this.name = name; }
    public void setSalary(double salary) {
        if (salary >= 0) this.salary = salary;
        else this.salary = 0;
    }

    public static int getTotalEmployees() { return totalEmployees; }

    // METHOD TO BE OVERRIDDEN (runtime polymorphism)
    public double calculateBonus() {
        return salary * 0.05;   // 5% basic bonus
    }

    public String toString() {
        return "ID: " + empId + " | " + name + " | $" + salary;
    }
}

// ========== 2. INHERITANCE (V3.0) ==========
class Manager extends Employee {
    private String department;

    public Manager(String name, double salary, String department) {
        super(name, salary);    // calling parent constructor
        this.department = department;
    }

    // METHOD OVERRIDING (runtime polymorphism)
    @Override
    public double calculateBonus() {
        return getSalary() * 0.15 + 2000;
    }

    @Override
    public String toString() {
        return super.toString() + " (Manager, Dept: " + department + ")";
    }
}

class Developer extends Employee {
    private String language;

    public Developer(String name, double salary, String language) {
        super(name, salary);
        this.language = language;
    }

    @Override
    public double calculateBonus() {
        return getSalary() * 0.10 + 1000;
    }

    @Override
    public String toString() {
        return super.toString() + " (Developer, Lang: " + language + ")";
    }
}

// ========== 3. MAIN GUI CLASS ==========
public class EmployeeMVP_GUI extends JFrame {
    private ArrayList<Employee> employees;
    private JTextArea displayArea;
    private JTextField nameField, salaryField, extraField;
    private JComboBox<String> typeCombo;

    public EmployeeMVP_GUI() {
        employees = new ArrayList<>();
        setTitle("Employee MVP - OOP Demo");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Employee"));

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Salary:"));
        salaryField = new JTextField();
        inputPanel.add(salaryField);

        inputPanel.add(new JLabel("Type:"));
        typeCombo = new JComboBox<>(new String[]{"Employee", "Manager", "Developer"});
        inputPanel.add(typeCombo);

        inputPanel.add(new JLabel("Dept (Manager) / Lang (Developer):"));
        extraField = new JTextField();
        inputPanel.add(extraField);

        JButton addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> addEmployee());
        inputPanel.add(addButton);

        JButton showAllButton = new JButton("Show All & Bonuses");
        showAllButton.addActionListener(e -> showAllEmployees());
        inputPanel.add(showAllButton);

        add(inputPanel, BorderLayout.NORTH);

        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // Status bar
        JLabel statusBar = new JLabel("Ready | OOP Concepts: Encapsulation, Inheritance, Polymorphism, Static");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);

        // Pre‑load some demo objects
        employees.add(new Manager("Sarah Johnson", 95000, "IT"));
        employees.add(new Developer("Mike Chen", 82000, "Java"));
        employees.add(new Developer("Elena Rodriguez", 79000, "Python"));
        employees.add(new Employee("John Smith", 45000));

        showAllEmployees();
    }

    // METHOD OVERLOADING (compile-time polymorphism)
    private void addEmployee() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter name");
            return;
        }
        double salary;
        try {
            salary = Double.parseDouble(salaryField.getText());
        } catch (Exception e) {
            salary = 0;
        }
        String type = (String) typeCombo.getSelectedItem();
        String extra = extraField.getText().trim();

        Employee emp = null;
        if (type.equals("Manager")) {
            emp = new Manager(name, salary, extra.isEmpty() ? "General" : extra);
        } else if (type.equals("Developer")) {
            emp = new Developer(name, salary, extra.isEmpty() ? "Java" : extra);
        } else {
            emp = new Employee(name, salary);
        }

        employees.add(emp);
        JOptionPane.showMessageDialog(this, "Added:\n" + emp);
        clearFields();
        showAllEmployees();
    }

    private void clearFields() {
        nameField.setText("");
        salaryField.setText("");
        extraField.setText("");
        typeCombo.setSelectedIndex(0);
    }

    private void showAllEmployees() {
        if (employees.isEmpty()) {
            displayArea.setText("No employees yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("========== ALL EMPLOYEES (Demonstrating Polymorphism) ==========\n\n");

        // SUPERCLASS REFERENCE LOOP - calls overridden calculateBonus() automatically
        for (Employee e : employees) {
            sb.append(e.toString()).append("\n");
            // Demonstrates runtime polymorphism: the right bonus method is called
            sb.append("   🎁 Bonus: $").append(String.format("%.2f", e.calculateBonus())).append("\n");
            sb.append("   ----------------------------------------\n");
        }

        // STATIC MEMBER ACCESS
        sb.append("\n📊 Total Employees Created: ").append(Employee.getTotalEmployees());
        sb.append("\n\n✅ Concepts demonstrated:\n");
        sb.append("   • Classes & Objects\n");
        sb.append("   • Encapsulation (private fields + getters/setters)\n");
        sb.append("   • Inheritance (Manager & Developer extend Employee)\n");
        sb.append("   • Method Overriding (calculateBonus())\n");
        sb.append("   • Method Overloading (constructors)\n");
        sb.append("   • Static Members (totalEmployees)\n");
        sb.append("   • Polymorphic ArrayList<Employee>\n");

        displayArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        // Run the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new EmployeeMVP_GUI().setVisible(true);
        });
    }
}