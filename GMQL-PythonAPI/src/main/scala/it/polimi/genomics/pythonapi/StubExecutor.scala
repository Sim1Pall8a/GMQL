package it.polimi.genomics.pythonapi

import it.polimi.genomics.GMQLServer.Implementation
import it.polimi.genomics.core.DataStructures.IRVariable
import it.polimi.genomics.core.ParsingType.PARSING_TYPE
import it.polimi.genomics.core.{GMQLLoaderBase, GRecordKey, GValue}

/**
  * Created by Luca Nanni on 16/05/17.
  * Email: luca.nanni@mail.polimi.it
  */
class StubExecutor extends Implementation {
  /** Starts the execution */
  override def go(): Unit = ???

  def collect(iRVariable: IRVariable): Any = ???

  def take(iRVariable: IRVariable, n: Int): Any = ???

  /** stop GMQL implementation (kill a job) */
  override def stop(): Unit = ???

  /** given the name of a parser, returns it. It must at least provide a parser for the "default" name */
  override def getParser(name: String, dataset: String): GMQLLoaderBase = ???

  override def collectIterator(iRVariable: IRVariable): (Iterator[(GRecordKey, Array[GValue])], Iterator[(Long, (String, String))], List[(String, PARSING_TYPE)]) = ???

  override def takeFirst(iRVariable: IRVariable, n: Int): (Array[(GRecordKey, Array[GValue])], Array[(Long, (String, String))], List[(String, PARSING_TYPE)]) = ???
}
