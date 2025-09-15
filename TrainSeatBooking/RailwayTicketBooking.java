import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

class ReservationEntity {
    private String name;

    public ReservationEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class Train extends ReservationEntity {
    private String time;
    private int sleeperStrength;
    private int acStrength;
    private int trainNumber;

    public Train(String name, String time, int sleeperStrength, int acStrength, int trainNumber) {
        super(name);
        this.time = time;
        this.sleeperStrength = sleeperStrength;
        this.acStrength = acStrength;
        this.trainNumber = trainNumber;
    }

    public String getTime() {
        return time;
    }

    public int getSleeperStrength() {
        return sleeperStrength;
    }

    public int getAcStrength() {
        return acStrength;
    }

    public int getTrainNumber() {
        return trainNumber;
    }
}

class Passenger extends ReservationEntity {
    private int age;
    private String gender;
    private String govtId;
    private String coachType;
    private int seatNumber;
    private double amount; // Added amount field

    public Passenger(String name, int age, String gender, String govtId, String coachType, int seatNumber, double amount) {
        super(name);
        this.age = age;
        this.gender = gender;
        this.govtId = govtId;
        this.coachType = coachType;
        this.seatNumber = seatNumber;
        this.amount = amount;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getGovtId() {
        return govtId;
    }

    public String getCoachType() {
        return coachType;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public double getAmount() {
        return amount;
    }
}

class Ticket {
    private Train train;
    private List<Passenger> passengers;

    public Ticket(Train train, List<Passenger> passengers) {
        this.train = train;
        this.passengers = passengers;
    }

    public Train getTrain() {
        return train;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }
}

public class RailwayTicketBooking {
    private static List<Train> trains = new ArrayList<>();
    private static List<Ticket> tickets = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static Map<Integer, Integer> lastSleeperSeatNumberMap = new HashMap<>();
    private static Map<Integer, Integer> lastACSeatNumberMap = new HashMap<>();

    public static void initializeTrains() {
        trains.add(new Train("Chennai - tiruvelveli", "13:05", 25, 25, 1010));
        trains.add(new Train("chennai - trichy", "7:00", 25, 25, 2013));
        trains.add(new Train("madurai - chennai", "10:00", 25, 25, 3045));
    }

    public static void displayTrainInformation() {
        System.out.println("Available Trains:");
        System.out.printf("%-20s%-10s%-20s%-15s%-15s%-15s\n", "Train Name", "Train No", "Departure Time", "Sleeper Seats",
                "AC Seats", "Price");
        for (Train train : trains) {
            int availableSleeperSeats = train.getSleeperStrength() - getBookedSleeperSeatsCount(train.getTrainNumber());
            int availableACSeats = train.getAcStrength() - getBookedACSeatsCount(train.getTrainNumber());
            double sleeperPrice = calculateAmount(train, "S");
            //double acPrice = calculateAmount(train, "AC");

            System.out.printf("%-20s%-10d%-20s%-15d%-15d%-15.2f\n", train.getName(), train.getTrainNumber(), train.getTime(),
                    availableSleeperSeats, availableACSeats, sleeperPrice);
        }
    }

    public static void checkSeatAvailability(Scanner scanner) {
        System.out.print("Enter the train number to check seat availability: ");
        int trainNumber = scanner.nextInt();
        Train selectedTrain = getTrainByNumber(trainNumber);

        if (selectedTrain != null) {
            int availableSleeperSeats = selectedTrain.getSleeperStrength() - getBookedSleeperSeatsCount(trainNumber);
            int availableACSeats = selectedTrain.getAcStrength() - getBookedACSeatsCount(trainNumber);

            System.out.println("Available seats for " + selectedTrain.getName() + " (Train No: " + trainNumber + "):");
            System.out.println("Sleeper Seats: " + availableSleeperSeats);
            System.out.println("AC Seats: " + availableACSeats);
        } else {
            System.out.println("Invalid train number. Please try again.");
        }
    }

    public static void bookTicket(Scanner scanner) {
        displayTrainInformation();
        System.out.print("Enter the train number: ");
        int trainNumber = scanner.nextInt();
        Train selectedTrain = getTrainByNumber(trainNumber);

        if (selectedTrain != null) {
            System.out.print("Enter the number of passengers: ");
            int passengerCount = scanner.nextInt();

            List<Passenger> passengers = new ArrayList<>();

            for (int i = 0; i < passengerCount; i++) {
                System.out.println("Enter details for Passenger " + (i + 1) + ":");
                System.out.print("Name: ");
                String name = getValidName(scanner);
                System.out.print("Age: ");
                int age = scanner.nextInt();
                System.out.print("Gender: ");
                String gender = scanner.next();
                System.out.print("Government ID (yes/no): ");
                String govtId = getValidGovtId(scanner);
                System.out.print("Choose coach type (Enter 'S' for Sleeper, 'AC' for AC): ");
                String coachType = getValidCoachType(scanner);

                // Assign seat number based on coach type
                int seatNumber = assignSeatNumber(selectedTrain, coachType);
                double amount = calculateAmount(selectedTrain, coachType);
                passengers.add(new Passenger(name, age, gender, govtId, coachType, seatNumber, amount));
            }

            Ticket ticket = new Ticket(selectedTrain, passengers);
            tickets.add(ticket);
            System.out.println("Ticket booked successfully!");
        } else {
            System.out.println("Invalid train number. Please try again.");
        }
    }

  private static double calculateAmount(Train train, String coachType) {
      double baseAmount = 50.0; // Adjust the base amount as needed
      if ("AC".equalsIgnoreCase(coachType)) {
          return baseAmount + 100.0; // Additional amount for AC coach
      } else if ("S".equalsIgnoreCase(coachType)) {
          return baseAmount;
      }
      return baseAmount;
  }




    private static String getValidCoachType(Scanner scanner) {
        String coachType;
        while (true) {
            coachType = scanner.next();
            if (!"AC".equalsIgnoreCase(coachType) && !"S".equalsIgnoreCase(coachType)) {
                System.out.println("Invalid input. Please enter 'AC' or 'S'.");
            } else {
                return coachType;
            }
        }
    }


    private static String getValidName(Scanner scanner) {
        String name = ""; // Initialize to an empty string
        while (true) {
            try {
                name = scanner.next();
                Integer.parseInt(name); // Try to parse the input as an Integer
                System.out.println("Invalid input. Please enter a valid name.");
            } catch (NumberFormatException e) {
                return name; // If it's not a number, return the name
            }
        }
    }

  private static String getValidGovtId(Scanner scanner) throws IllegalArgumentException {
      String govtId = ""; // Initialize to an empty string
      while (true) {
          try {
              govtId = scanner.next();
              if (!"yes".equalsIgnoreCase(govtId) && !"no".equalsIgnoreCase(govtId)) {
                  throw new IllegalArgumentException("Invalid input. Please enter 'yes' or 'no'.");
              }
              if ("no".equalsIgnoreCase(govtId)) {
                  throw new IllegalArgumentException("Government ID cannot be 'no'. Exiting program.");
              }
              return govtId;
          } catch (IllegalArgumentException e) {
              System.out.println(e.getMessage());
              if ("no".equalsIgnoreCase(govtId)) {
                  System.exit(0); // Exit the program if government ID is 'no'
              }
          }
      }
  }


  public static void cancelTicket(Scanner scanner) {
      System.out.print("Enter the train number to cancel the ticket: ");
      int trainNumber = scanner.nextInt();

      Train selectedTrain = getTrainByNumber(trainNumber);

      if (selectedTrain != null) {
          System.out.print("Enter the ticket number to cancel: ");
          int ticketNumber = scanner.nextInt();

          if (ticketNumber > 0 && ticketNumber <= tickets.size()) {
              Ticket cancelledTicket = tickets.remove(ticketNumber - 1);
              Train train = cancelledTicket.getTrain();
              List<Passenger> cancelledPassengers = cancelledTicket.getPassengers();

              System.out.println("Ticket cancelled successfully!");
              System.out.println("Cancelled Ticket Details:");
              System.out.println("Train Name: " + train.getName());
              System.out.println("Departure Time: " + train.getTime());
              System.out.println("Cancelled Passenger Details:");
              for (Passenger passenger : cancelledPassengers) {
                  System.out.println("Name: " + passenger.getName());
                  System.out.println("Age: " + passenger.getAge());
                  System.out.println("Gender: " + passenger.getGender());
                  System.out.println("Government ID: " + passenger.getGovtId());
                  System.out.println("Coach Type: " + passenger.getCoachType());
                  System.out.println("Seat Number: " + passenger.getSeatNumber());
                  System.out.println();
              }
          } else {
              System.out.println("Invalid ticket number. Please try again.");
          }
      } else {
          System.out.println("Invalid train number. Please try again.");
      }
  }


  public static void displayTickets() {
      System.out.println("Booked Tickets:");
      for (int i = 0; i < tickets.size(); i++) {
          Ticket ticket = tickets.get(i);
          System.out.println("Ticket " + (i + 1) + ":");
          System.out.println("Train Name: " + ticket.getTrain().getName());
          System.out.println("Departure Time: " + ticket.getTrain().getTime());
          System.out.println("Passenger Details:");

          List<Passenger> passengers = ticket.getPassengers();

          for (Passenger passenger : passengers) {
              System.out.println("Name: " + passenger.getName());
              System.out.println("Age: " + passenger.getAge());
              System.out.println("Gender: " + passenger.getGender());
              System.out.println("Government ID: " + passenger.getGovtId());
              System.out.println("Coach Type: " + passenger.getCoachType());
              System.out.println("Seat Number: " + passenger.getSeatNumber());

              // Print additional information based on coach type
              double amount = calculateAmount(ticket.getTrain(), passenger.getCoachType());
              System.out.println("Price for " + passenger.getCoachType() + " Coach: $" + amount);

              System.out.println();
          }
      }
  }





    private static int assignSeatNumber(Train train, String coachType) {
        int seatNumber = -1;
        if ("S".equalsIgnoreCase(coachType)) {
            seatNumber = getAvailableSleeperSeat(train);
        } else if ("AC".equalsIgnoreCase(coachType)) {
            seatNumber = getAvailableACSeat(train);
        }
        return seatNumber;
    }

    private static int getAvailableSleeperSeat(Train train) {
        int bookedSleeperSeats = getBookedSleeperSeatsCount(train.getTrainNumber());
        if (bookedSleeperSeats < train.getSleeperStrength()) {
            int lastSleeperSeatNumber = lastSleeperSeatNumberMap.getOrDefault(train.getTrainNumber(), 0);
            lastSleeperSeatNumber++;
            lastSleeperSeatNumberMap.put(train.getTrainNumber(), lastSleeperSeatNumber);
            return lastSleeperSeatNumber;
        }
        return -1; // No available sleeper seats
    }

    private static int getAvailableACSeat(Train train) {
        int bookedACSeats = getBookedACSeatsCount(train.getTrainNumber());
        if (bookedACSeats < train.getAcStrength()) {
            int lastACSeatNumber = lastACSeatNumberMap.getOrDefault(train.getTrainNumber(), 0);
            lastACSeatNumber++;
            lastACSeatNumberMap.put(train.getTrainNumber(), lastACSeatNumber);
            return lastACSeatNumber;
        }
        return -1; // No available AC seats
    }

    private static int getBookedSleeperSeatsCount(int trainNumber) {
        int bookedSleeperSeats = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getTrain().getTrainNumber() == trainNumber) {
                List<Passenger> passengers = ticket.getPassengers();
                for (Passenger passenger : passengers) {
                    if ("S".equalsIgnoreCase(passenger.getCoachType())) {
                        bookedSleeperSeats++;
                    }
                }
            }
        }
        return bookedSleeperSeats;
    }

    private static int getBookedACSeatsCount(int trainNumber) {
        int bookedACSeats = 0;
        for (Ticket ticket : tickets) {
            if (ticket.getTrain().getTrainNumber() == trainNumber) {
                List<Passenger> passengers = ticket.getPassengers();
                for (Passenger passenger : passengers) {
                    if ("AC".equalsIgnoreCase(passenger.getCoachType())) {
                        bookedACSeats++;
                    }
                }
            }
        }
        return bookedACSeats;
    }

    private static Train getTrainByNumber(int trainNumber) {
        for (Train train : trains) {
            if (train.getTrainNumber() == trainNumber) {
                return train;
            }
        }
        return null;
    }
  public static void displayTicketsWithAmount() {
      System.out.println("Booked Tickets:");
      for (int i = 0; i < tickets.size(); i++) {
          Ticket ticket = tickets.get(i);
          System.out.println("Ticket " + (i + 1) + ":");
          System.out.println("Train Name: " + ticket.getTrain().getName());
          System.out.println("Departure Time: " + ticket.getTrain().getTime());
          System.out.println("Passenger Details:");

          List<Passenger> passengers = ticket.getPassengers();
          double totalAmount = 0;

          for (Passenger passenger : passengers) {
              System.out.println("Name: " + passenger.getName());
              System.out.println("Age: " + passenger.getAge());
              System.out.println("Gender: " + passenger.getGender());
              System.out.println("Government ID: " + passenger.getGovtId());
              System.out.println("Coach Type: " + passenger.getCoachType());
              System.out.println("Seat Number: " + passenger.getSeatNumber());
              System.out.println("Amount: $" + passenger.getAmount());
              totalAmount += passenger.getAmount();
              System.out.println();
          }

          System.out.println("Total Amount for Ticket " + (i + 1) + ": $" + totalAmount);
          System.out.println();
      }
  }



    public static void main(String[] args) {
        initializeTrains();

        while (true) {
            System.out.println("\nRailway Reservation System Menu:");
            System.out.println("1. Display Train Information");
            System.out.println("2. Check Seat Availability");
            System.out.println("3. Book a Ticket");
            System.out.println("4. Cancel a Ticket");
            System.out.println("5. Display Booked Tickets with Amount");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    displayTrainInformation();
                    break;
                case 2:
                    checkSeatAvailability(scanner);
                    break;
                case 3:
                    bookTicket(scanner);
                    break;
                case 4:
                    cancelTicket(scanner);
                    break;
                case 5:
                    displayTicketsWithAmount();
                    break;
                case 6:
                    System.out.println("Thank you for using the Railway Reservation System!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}