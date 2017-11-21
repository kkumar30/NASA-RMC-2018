using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GameControllerInterface
{
    interface IActionQueue
    {
        int getTime();

        Byte[] generateMessage();
        String getDescription();
        String getActionType();
    }
}
