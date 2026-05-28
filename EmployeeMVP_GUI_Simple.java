import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
 
class Employee {
    private static int nextId = 1000;
    private static int totalEmployees = 0;
    private int empId;
    private String name;
    private double salary;

    // Overloaded constructors (compile-time polymorphism)
    public Employee(String name, double salary) {
        this.name = name;
        setSalary(salary);   // validation inside setter
        this.empId = nextId++;
        totalEmployees++;
    }
    public Employee(String name) {
        this(name, 0.0);
    }

    // Encapsulation: getters and setters
    public int getEmpId() { return empId; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public void setName(String name) { this.name = name; }
    public void setSalary(double salary) {
        this.salary = Math.max(salary, 0);
    }
    public static int getTotalEmployees() { return totalEmployees; }

    // Method to be overridden (runtime polymorphism)
    public double calculateBonus() {
        return salary * 0.05;   // 5%
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %-12s | $%8.2f", empId, name, salary);
    }
}

class Manager extends Employee {
    private String department;
    public Manager(String name, double salary, String department) {
        super(name, salary);
        this.department = department;
    }
    @Override
    public double calculateBonus() {
        return getSalary() * 0.15 + 2000;
    }
    @Override
    public String toString() {
        return super.toString() + " | Manager | " + department;
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
        return super.toString() + " | Developer | " + language;
    }
}

public class EmployeeMVP_GUI_Simple extends JFrame {
    private ArrayList<Employee> employees;
    private JTextArea displayArea;
    private JTextField nameField, salaryField, extraField;
    private JComboBox<String> typeCombo;

    public EmployeeMVP_GUI_Simple() {
        employees = new ArrayList<>();
        setTitle("Employee Management MVP - OOP Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 550);
        setLocationRelativeTo(null); // center on screen
        setLayout(new BorderLayout(10, 10));

        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Employee"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        // Row 1: Salary
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Salary ($):"), gbc);
        gbc.gridx = 1;
        salaryField = new JTextField(15);
        formPanel.add(salaryField, gbc);

        // Row 2: Type
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        typeCombo = new JComboBox<>(new String[]{"Employee", "Manager", "Developer"});
        formPanel.add(typeCombo, gbc);

        // Row 3: Extra field (Department for Manager, Language for Developer)
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Dept (Mgr) / Lang (Dev):"), gbc);
        gbc.gridx = 1;
        extraField = new JTextField(15);
        formPanel.add(extraField, gbc);

        // Row 4: Buttons
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("➕ Add Employee");
        JButton refreshBtn = new JButton("🔄 Refresh List");
        buttonPanel.add(addBtn);
        buttonPanel.add(refreshBtn);
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.NORTH);

        
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Employee List (with Bonuses)"));
        add(scrollPane, BorderLayout.CENTER);

        
        JLabel statusBar = new JLabel(" OOP Concepts: Classes, Encapsulation, Inheritance, Polymorphism, Static");
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        add(statusBar, BorderLayout.SOUTH);

        
        employees.add(new Manager("Sarah Johnson", 95000, "IT"));
        employees.add(new Developer("Mike Chen", 82000, "Java"));
        employees.add(new Developer("Elena Rodriguez", 79000, "Python"));
        employees.add(new Employee("John Smith", 45000));

        
        addBtn.addActionListener(e -> addEmployee());
        refreshBtn.addActionListener(e -> refreshDisplay());

        
        refreshDisplay();
    }

    private void addEmployee() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name.");
            return;
        }
        double salary = 0;
        try {
            salary = Double.parseDouble(salaryField.getText().trim());
        } catch (Exception e) {
            // salary stays 0
        }
        String type = (String) typeCombo.getSelectedItem();
        String extra = extraField.getText().trim();

        Employee emp = null;
        switch (type) {
            case "Manager":
                emp = new Manager(name, salary, extra.isEmpty() ? "General" : extra);
                break;
            case "Developer":
                emp = new Developer(name, salary, extra.isEmpty() ? "Java" : extra);
                break;
            default:
                emp = new Employee(name, salary);
                break;
        }
        employees.add(emp);
        JOptionPane.showMessageDialog(this, "✅ Employee added:\n" + emp);
        nameField.setText("");
        salaryField.setText("");
        extraField.setText("");
        typeCombo.setSelectedIndex(0);
        refreshDisplay();
    }

    private void refreshDisplay() {
        if (employees.isEmpty()) {
            displayArea.setText("No employees yet. Add some using the form above.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("========== ALL EMPLOYEES (Polymorphism in action) ==========\n\n");
        for (Employee e : employees) {
            sb.append(e.toString()).append("\n");
            sb.append("   🎁 Bonus: $").append(String.format("%.2f", e.calculateBonus())).append("\n");
            sb.append("   -------------------------------------------------\n");
        }
        sb.append("\n📊 Total Employees Created: ").append(Employee.getTotalEmployees());
        sb.append("\n\n✅ OOP Concepts shown:\n");
        sb.append("   • Classes & Objects (V1.0)\n");
        sb.append("   • Encapsulation (private fields + getters/setters)\n");
        sb.append("   • Inheritance (Manager & Developer extend Employee)\n");
        sb.append("   • Method Overriding (calculateBonus() differs per role)\n");
        sb.append("   • Method Overloading (constructors)\n");
        sb.append("   • Static Members (totalEmployees)\n");
        sb.append("   • Polymorphic ArrayList<Employee>\n");
        displayArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeMVP_GUI_Simple().setVisible(true));
    }
}
