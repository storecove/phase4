package com.helger.as4.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.GuardedBy;

import com.helger.commons.collection.ext.CommonsHashMap;
import com.helger.commons.collection.ext.ICommonsMap;
import com.helger.commons.concurrent.SimpleReadWriteLock;
import com.helger.commons.io.resource.ClassPathResource;
import com.helger.commons.state.EContinue;

/**
 * This is the duplicate checker for receiving. <br>
 * TODO Duplicate check implementation is dumb because it uses an unlimited set
 * and does not persist the data. Suggestions for improvement: 1) limit number
 * of entries - ideally with a time constraint. 2) persist the data so that a
 * restart of the server does not alter working of duplicate checking. 3) Create
 * a job that regularly (e.g. every 10 minutes) removes all entries that are
 * older than a certain threshold.
 *
 * @author Philip Helger
 */
public final class AS4DuplicateChecker
{
  private static final long ONE_MINUTE = 60000;

  private static final SimpleReadWriteLock s_aRWLock = new SimpleReadWriteLock ();
  @GuardedBy ("s_aRWLock")
  private static final ICommonsMap <String, Long> s_aMap = new CommonsHashMap <> ();

  private static long s_nMinutesToRefresh = 0;
  private static File aSaveFile;

  private AS4DuplicateChecker ()
  {}

  /**
   * Check if the passed message ID was already handled.
   *
   * @param sMessageID
   *        Message ID to check. May be <code>null</code>.
   * @return {@link EContinue#CONTINUE} to continue
   */
  @Nonnull
  public static EContinue registerAndCheck (@Nullable final String sMessageID)
  {
    if (s_aMap.containsKey (sMessageID))
    {
      return EContinue.BREAK;
    }
    s_aRWLock.writeLocked ( () -> {
      final long aValue = System.currentTimeMillis ();
      s_aMap.put (sMessageID, aValue);
      try
      {
        Files.write (aSaveFile.toPath (), (sMessageID + " " + aValue + "\n").getBytes (), StandardOpenOption.APPEND);
      }
      catch (final IOException e)
      {
        e.printStackTrace ();
      }
    });

    return EContinue.CONTINUE;
  }

  /**
   * Remove all entries in the cache.
   */
  public static void clearCache ()
  {
    s_aRWLock.writeLocked ( () -> s_aMap.clear ());
  }

  public static void startDuplicateService (final long nMinutesToRefresh)
  {
    final String sFilepath = "ReceivedUserMessages.txt";
    aSaveFile = ClassPathResource.getAsFile (sFilepath);
    // Create file if it does not exist, else clear messages
    if (aSaveFile == null)
      aSaveFile = new File (sFilepath);
    else
    {
      try
      {
        final List <String> aList = Files.readAllLines (aSaveFile.toPath ());
        for (final String sEntry : aList)
        {
          final String [] aKeyValuePair = sEntry.split (" ");
          s_aMap.put (aKeyValuePair[0], Long.parseLong (aKeyValuePair[1]));
        }
      }
      catch (final IOException e)
      {
        e.printStackTrace ();
      }
      clearDisposableMessages ();
    }

    s_nMinutesToRefresh = nMinutesToRefresh;
    final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor ();
    service.scheduleWithFixedDelay ( () -> clearDisposableMessages (), 0, nMinutesToRefresh, TimeUnit.MINUTES);
  }

  private static void clearDisposableMessages ()
  {
    // Current time - 60000 (which equals 1 Minute) times the minutes
    // specified
    final long nTimeToCheckAgainst = System.currentTimeMillis () - ONE_MINUTE * s_nMinutesToRefresh;
    for (final String aKey : s_aMap.keySet ())
    {
      final Long aValue = s_aMap.get (aKey);

      if (aValue < nTimeToCheckAgainst)
      {
        s_aMap.remove (aKey);
      }
    }
  }

}
