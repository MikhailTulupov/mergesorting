package ru.tulupov;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.BlockingDeque;

import static java.util.stream.Collectors.toList;

public class SorterImpl implements Sorter, MediatorsSubscriber {

    /**
     * This variable write sorting data in file
     */
    private PrintWriter out;

    /**
     * This list contains end of files.
     */
    private final List<BlockingDeque<String>> eofList;

    public SorterImpl(String outputFileName) {
        eofList = new ArrayList<>();
        try {
            this.out = new PrintWriter(outputFileName);
        } catch (FileNotFoundException exception) {
            System.err.println("Can`t create output file " + exception.getMessage());
            System.exit(8);
        }
    }

    @Override
    public void notify(BlockingDeque<String> deque) {
        eofList.add(deque);
    }

    @Override
    public void sort(List<BlockingDeque<String>> deques) {
        while (true) {
            cleanFinishedDeques(deques);
            if (deques.size() == 0) break;

            BlockingDeque<String> actualDeque = null;
            try {
                actualDeque = getActualDeque(deques);
                if (failedSortOrder(actualDeque)) {
                    deques.remove(actualDeque);
                    continue;
                }
            } catch (NumberFormatException e) {
                deques.remove(actualDeque);
                continue;
            } catch (IllegalArgumentException e) {
                continue;
            }

            try {
                if (actualDeque.size() > 0) out.println(actualDeque.takeFirst());
            } catch (InterruptedException ignored) {
            }

        }
    }

    private boolean failedSortOrder(BlockingDeque<String> deque) {
        if (deque.peekFirst() == null || deque.peekLast() == null) return false;
        if (Launcher.isStrings) {
            if (Launcher.isAscending) {
                return (deque.peekFirst()).compareTo(deque.peekLast()) > 0;
            } else {
                return (deque.peekFirst()).compareTo(deque.peekLast()) < 0;
            }
        } else {
            if (Launcher.isAscending) {
                return string2integer(deque.peekFirst()) > string2integer(deque.peekLast());
            } else {
                return string2integer(deque.peekFirst()) < string2integer(deque.peekLast());
            }
        }
    }

    private void cleanFinishedDeques(List<BlockingDeque<String>> deques) {
        for (BlockingDeque<String> deque : deques) {
            if (eofList.contains(deque) && deque.size() == 0) {
                deques.remove(deque);
                eofList.remove(deque);
            }
        }
    }

    private BlockingDeque<String> getActualDeque(List<BlockingDeque<String>> deques)
            throws IllegalArgumentException {
        if (deques.size() == 1) return deques.get(0);
        String[] values = new String[deques.size()];
        BlockingDeque<String>[] dequesArray = new BlockingDeque[deques.size()];
        int i = 0;
        for (BlockingDeque<String> deque : deques) {
            if (deque.peekFirst() == null) continue;
            values[i] = deque.peekFirst();
            dequesArray[i] = deque;
            i++;
        }

        if (Arrays.stream(values).anyMatch(Objects::isNull))
            throw new IllegalArgumentException("due over fast grabbing");

        if (Launcher.isStrings) {
            if (Launcher.isAscending) {
                return dequesArray[findIndexForStringsMinValue(values)];
            } else {
                return dequesArray[findIndexForStringsMaxValue(values)];
            }
        } else {
            if (Launcher.isAscending) {
                return dequesArray[findIndexForIntegersMinValue(values)];
            } else {
                return dequesArray[findIndexForIntegersMaxValue(values)];
            }
        }
    }

    private int findIndexForStringsMaxValue(String[] strings) {
        if (strings.length == 1) return 0;
        Optional<String> maximum = Arrays.stream(strings).max(Comparator.comparing(String::toString));
        return Arrays.stream(strings).toList().indexOf(maximum.get());
    }

    private int findIndexForStringsMinValue(String[] strings) {
        if (strings.length == 1) return 0;
        Optional<String> minimum = Arrays.stream(strings).min(Comparator.comparing(String::toString));
        return Arrays.stream(strings).collect(toList()).indexOf(minimum.get());
    }

    private int findIndexForIntegersMinValue(String[] numbers) throws NumberFormatException {
        if (numbers.length == 1) return 0;
        Optional<Integer> iMinimum = Arrays.stream(numbers).map(this::string2integer).min(Comparator.comparingInt(Integer::intValue));
        Optional<String> minimum = Optional.of(iMinimum.get().toString());
        return Arrays.stream(numbers).collect(toList()).indexOf(minimum.get());
    }

    private int findIndexForIntegersMaxValue(String[] numbers) throws NumberFormatException {
        if (numbers.length == 1) return 0;
        Optional<Integer> iMaximum = Arrays.stream(numbers).map(this::string2integer).max(Comparator.comparingInt(Integer::intValue));
        Optional<String> maximum = Optional.of(iMaximum.get().toString());
        return Arrays.stream(numbers).collect(toList()).indexOf(maximum.get());
    }


    @Override
    public void close() throws IOException {
        if (out != null) out.close();
    }

    private Integer string2integer(String string) throws NumberFormatException {
        try {
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Нарушение формата чисел в одном из входных файлов. " +
                    "Файл исключен из обработки. В строке '" + string +
                    "'. Причина '" + e.getMessage() + "'");
        }
    }
}
