# Sorting Algorithm Visualization
## Overview
This project is a graphical visualization of six popular sorting algorithms: Bubble Sort, Selection Sort, Insertion Sort, Merge Sort, Quick Sort, and Shell Sort. The application provides an interactive interface where users can see these algorithms in action, helping to understand how they work step-by-step.

## Features
Interactive Visualization: Watch each algorithm sort a list in real-time.
Main Menu: A central hub to select and configure the sorting visualizations.
Algorithm Details: Each sorting method is accompanied by a brief description and its time complexity.
Customizable Settings: Adjust the speed of the visualization, the size of the dataset to sort, stop and continue the sorting process using threads.

## Sorting Algorithms
1. Bubble Sort
Bubble Sort is a simple, comparison-based algorithm that repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order. The process is repeated until the list is sorted.
Time Complexity: O(n²)
Description: Easy to understand but inefficient for large datasets.

2. Selection Sort
Selection Sort divides the input list into two parts: the sorted part at the beginning and the unsorted part at the end. It repeatedly selects the smallest (or largest) element from the unsorted part and moves it to the end of the sorted part.
Time Complexity: O(n²)
Description: Simple and intuitive but also inefficient for large lists.

3. Insertion Sort
Insertion Sort builds the final sorted list one item at a time. It picks an element from the unsorted portion and inserts it into the correct position in the sorted portion.
Time Complexity: O(n²)
Description: Efficient for small datasets and nearly sorted lists.

4. Merge Sort
Merge Sort is a divide-and-conquer algorithm that splits the list into two halves, recursively sorts each half, and then merges the two sorted halves into a single sorted list.
Time Complexity: O(n log n)
Description: Efficient for larger datasets, stable and parallelizable.

5. Quick Sort
Quick Sort is another divide-and-conquer algorithm that selects a pivot element and partitions the list into two sub-arrays: elements less than the pivot and elements greater than the pivot. It then recursively sorts the sub-arrays.
Time Complexity: O(n log n) on average
Description: Very efficient on average, but performance can degrade to O(n²) in the worst case.

6. Shell Sort
Shell Sort is an extension of Insertion Sort that allows the exchange of items that are far apart. The algorithm uses a gap sequence to determine the distance between elements compared and moved.
Time Complexity: O(n log n) on average
Description: Faster than Insertion Sort and Bubble Sort for larger lists.

Main Menu
The main menu serves as the control center of the application. From here, you can:
View the short introduction about sorting.
View details about our projects and some guide about how to use the sorting visualization.
View details about each algorithm's complexity and use case.
Choose a sorting algorithm to visualize.

Customization
In the settings menu, you can:

Adjust Speed: Control the animation speed to see the sorting process more clearly.
Dataset Size: Increase or decrease the number of elements in the list to be sorted.

Contribution
Contributions are welcome! Please fork the repository and submit a pull request with your improvements. You can also open issues for bugs or feature requests.

License
This project is licensed under the MIT License. See the LICENSE file for details.

Contact
For any questions or suggestions, please reach out to vuonglebinhduong2004@email.com.

Thank you for using the Sorting Algorithm Visualization tool. We hope it helps you understand these fundamental algorithms better!
